package rzab;

import java.lang.instrument.Instrumentation;
import java.util.*;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import rzab.code.js.ToJs;
import rzab.code.rl.RL;
import rzab.code.rl.parser.Parser;
import rzab.code.rl.parser.Script;
import rzab.code.rl.parser.exception.DefArgLengthException;
import rzab.code.tools.DynamicPause;
import rzab.gui.WebLoader;

public class DebugAgent {
    public static String[] exclude = new String[]{"public void rzab.code.tools.DynamicPause$PauseRequest.call(java.lang.String)",
            "public static void rzab.code.tools.DynamicPause.enter(java.lang.String)",
            "public static void rzab.code.js.ToJs.pushMethod(java.lang.String)"};
    public static Instrumentation inst;
    public static boolean test;

    public static void premain(String agentArgs, Instrumentation inste) {
       // Parser.parse();
        inst = inste;
        new Thread(() -> new WebLoader().runLauncher()).start();
        while (!test) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        DynamicPause.getRequest("public java.lang.Object rzab.ProtectSetup$DefinedMethod.call(java.lang.Object[])");
    }

    public static void pause() {
        if (!pause) {
            pause = true;
            ToJs.pauseState(true);
        }
    }

    public static void resume() {
        pause = false;
        pauseList.forEach(Pause::resume);
        pauseList.clear();
        ToJs.pauseState(false);
    }

    public static boolean pause = false;

    public static void loaded() {
        new AgentBuilder.Default()
                .type(ElementMatchers.isSubTypeOf(Object.class))
                .transform((builder, typeDescription, classLoader, javaModule, dynamicTypeBuilder) ->
                        builder.visit(Advice.to(MethodAdvice.class).on(ElementMatchers.isMethod())))
                .installOn(inst);
        test = true;
    }

    public static class Pause {
        public boolean aBoolean;

        public Pause(boolean aBoolean) {
            this.aBoolean = aBoolean;
        }

        public void resume() {
            this.aBoolean = false;
        }
    }

    public static List<Pause> pauseList = new ArrayList<>();

    public static boolean contains(String s) {
        for (String value : exclude) {
            if (value.equals(s))
                return true;
        }
        return false;
    }

    public static class MethodAdvice {

        @Advice.OnMethodEnter()
        public static void enter(@Advice.Origin String method) {
            //@Advice.This(optional = true) Object instance, @Advice.AllArguments(readOnly = false) Object[] args
            System.out.println(method);
            if (pause && !method.contains("com.sun.") && !method.contains("javafx.")) {
                Pause pause = new Pause(true);
                pauseList.add(pause);
                while (pause.aBoolean) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println(method);

            if (!contains(method) && !method.contains("com.sun.") && !method.contains("javafx.")) {
                ToJs.pushMethod(0, method);
            }
        }

        @Advice.OnMethodExit
        public static void exit(@Advice.Origin String method, @Advice.Return(readOnly = false) Object returnValue) throws DefArgLengthException {
            if (!contains(method) && !method.contains("com.sun.") && !method.contains("javafx.")) {
                DynamicPause.PauseRequest req;
                if (returnValue != null) {
                    req = DynamicPause.first(method, returnValue);
                } else
                    req = DynamicPause.first(method, null);
//                if (returnValue != null)
//                    System.out.println((req == null ? "X" : "D") + " :" + returnValue.getClass().getName());
                Script script = RL.scriptMap.get(method);
                if (req != null)
                    returnValue = script != null? script.getValue() : req.arg;
                ToJs.pushMethod(1, method);
                // System.out.println(returnValue);
            }
        }
    }
}

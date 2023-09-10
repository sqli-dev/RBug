package rzab.code.rl.parser;

import rzab.code.rl.RL;
import rzab.code.rl.parser.exception.DefArgLengthException;

public class Script {
    public String methodName;
    public Parser parser;
    public Object initial;

    public Script(String methodName, String script, Object initial, int maxFire) {
        this.methodName = methodName;
        this.parser = new Parser(script);
        this.parser.preInit();
        this.initial = initial;
    }

    public Object getValue() throws DefArgLengthException {
        if(parser.fired >= parser.fireMax) {
            RL.scriptMap.remove(methodName);
            return initial;
        }
        return parser.parse(methodName,initial);
    }
}

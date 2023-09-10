package rzab.code.rl.parser;

import rzab.code.rl.definitions.compare.Compare;
import rzab.code.rl.parser.exception.DefArgLengthException;
import rzab.code.rl.parser.modules.VarParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
    private String script;

    List<String> codeLines = new ArrayList<>();
    public int fired;
    public int fireMax;

    public Parser(String script) {
        this.script = script;
        this.fired = 0;
        this.fireMax = 1;
    }

    public void preInit() {
        String[] lines = script.split(";");
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].startsWith("//"))
                codeLines.add(lines[i].trim().replaceAll("\\s{2,}", " "));
        }
        if (codeLines.get(0).startsWith("*") && codeLines.get(0).length() != 1) {
            fireMax = Integer.parseInt(codeLines.get(0).substring(1));
        }
    }

    public Object parse(String method, Object returns) throws DefArgLengthException {
        Map<String, ParsableObject> definitionMap = new HashMap<>();
        definitionMap.put("mname", new ParsableObject.ParsableString(method));
        definitionMap.put("voidname", new ParsableObject.ParsableString(method));
        Object returnable = returns;

        boolean applyMod = false;

        for (int i = 0; i < codeLines.size(); i++) {
            String line = codeLines.get(i);
            String[] types = line.split(" ");
            switch (types[0].toLowerCase()) {
                case "fire":
                    fired++;
                    break;
                case "mark":
                    if (types.length == 4) {
                        definitionMap.put(types[1], new VarParser().parseObject(types));
                    }
                    break;
                case "push":
                    if (types.length == 3 || types.length == 2) {
                        Object ret = null;
                        if (!types[1].equals("null"))
                            ret = getType(types[1], definitionMap);
                        if (types.length == 3) {
                            if (types[2].equalsIgnoreCase("safecast")) {
                                if (ret != null)
                                    if (!returns.getClass().isInstance(ret.getClass())) {
                                        try {
                                            returnable = returnable.getClass().cast(ret);
                                        } catch (Exception e) {
                                            System.out.println("Couldn't cast in: " + line);
                                        }
                                    }
                            } else {
                                throw new RuntimeException("Unknown definition in: " + line);
                            }
                        } else {
                            returnable = returnable.getClass().cast(ret);
                        }
                    } else {
                        throw new DefArgLengthException(2, types.length, line);
                    }
                    break;
            }

            if (Compare.getType(types[0]) != null) {
                applyMod = Compare.compared(types[0], getType(types[1], definitionMap), getType(types[2], definitionMap));
            }
        }
        return applyMod ? returnable : returns;
    }

    public Object getType(String type, Map<String, ParsableObject> map) {
        if (type.startsWith("^")) {
            return map.get(type.substring(1)).parsedValue();
        }
        return type;
    }
}

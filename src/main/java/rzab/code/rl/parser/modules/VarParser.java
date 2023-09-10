package rzab.code.rl.parser.modules;

import rzab.code.rl.parser.ParsableObject;

public class VarParser {

    public ParsableObject parseObject(String[] splitted) {
        if(splitted[2].equals("asnum")) {
            return new ParsableObject.ParsableNum(splitted[3]);
        }else if(splitted[2].equals("astext")) {
            return new ParsableObject.ParsableString(splitted[3]);
        }
        throw new RuntimeException("Unknown astype");
    }
}

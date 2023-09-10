package rzab.code.rl.definitions.compare;

public class Compare {
    public enum CompareType {
        GE(array("GE", "GOE", "GEQ"), (o1, o2) -> {
            if (o1 instanceof Number && o2 instanceof Number) {
                return ((Number) o1).doubleValue() >= ((Number) o2).doubleValue();
            }
            return false;
        }),
        EQ(array("E", "EQ", "IS", "ET"), (o1, o2) -> {
            if (o1 instanceof Number && o2 instanceof Number) {
                return ((Number) o1).doubleValue() == ((Number) o2).doubleValue();
            }
            return o1 != null && o1.equals(o2);
        }),
        LE(array("LE", "LOE", "LEQ"), (o1, o2) -> {
            if (o1 instanceof Number && o2 instanceof Number) {
                return ((Number) o1).doubleValue() <= ((Number) o2).doubleValue();
            }
            return false;
        }),
        GT(array("GT", "G", "GREATER"), (o1, o2) -> {
            if (o1 instanceof Number && o2 instanceof Number) {
                return ((Number) o1).doubleValue() > ((Number) o2).doubleValue();
            }
            return false;
        }),
        LT(array("LT", "L", "LESS"), (o1, o2) -> {
            if (o1 instanceof Number && o2 instanceof Number) {
                return ((Number) o1).doubleValue() < ((Number) o2).doubleValue();
            }
            return false;
        });

        String[] defs;
        ICompare comparer;

        CompareType(String[] defs, ICompare comparer) {
            this.defs = defs;
            this.comparer = comparer;
        }
    }

    public static CompareType getType(String name) {
        CompareType type = null;
        for (int i = 0; i < CompareType.values().length; i++) {
            CompareType ct = CompareType.values()[i];
            for (int i1 = 0; i1 < ct.defs.length; i1++) {
                if (ct.defs[i1].equalsIgnoreCase(name)) {
                    if (type == null)
                        type = ct;
                    else
                        throw new RuntimeException("More than 1 comparer in expression");
                }
            }
        }
        return type;
    }
    public static boolean compared(String name, Object o1, Object o2) {
        CompareType type = getType(name);
        return type.comparer.compare(o1, o2);
    }

    //public static void
    public static String[] array(String... s) {
        return s;
    }
}

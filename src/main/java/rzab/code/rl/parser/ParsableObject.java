package rzab.code.rl.parser;

public abstract class ParsableObject {

    public Object object;
    public String value;

    public ParsableObject(String value) {
        this.value = value;
    }

    public Object parsedValue() {
        return object;
    }

    public static class ParsableNum extends ParsableObject {

        public ParsableNum(String value) {
            super(value);
            switch (value.charAt(value.length() - 1)) {
                case 'l':
                    if(value.contains(".")) throw new RuntimeException("Unexpected point in long value");
                    this.object = Long.parseLong(value);
                    break;
                case 'f':
                    this.object = Float.parseFloat(value);
                    break;
                case 'd':
                    this.object = Double.parseDouble(value);
                    break;
                default:
                    if(value.contains(".")) throw new RuntimeException("Unexpected point in integer value");
                    this.object = Integer.parseInt(value);
                    break;
            }
        }
    }

    public static class ParsableString extends ParsableObject {

        public ParsableString(String value) {
            super(value);
            this.object = value;
        }
    }
}

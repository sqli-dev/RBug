package rzab.code.rl.parser.exception;

public class DefArgLengthException extends Exception {

    public DefArgLengthException(int expected, int provided, String fullLine) {
        super(String.format("Expected %s arg lenght but %s provided\nError at: %s",expected,provided,fullLine));
    }
}

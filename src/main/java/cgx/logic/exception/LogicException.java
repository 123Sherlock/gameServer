package cgx.logic.exception;

public class LogicException extends Exception {

    public LogicException(int code) {
        _code = code;
    }

    public int getCode() {
        return _code;
    }

    private final int _code;
}

package cgx.core.behavior;

public class BehaviorNameException extends RuntimeException {

    public BehaviorNameException(String className) {
        super(String.format("behavior命名有误：%s", className));
    }
}

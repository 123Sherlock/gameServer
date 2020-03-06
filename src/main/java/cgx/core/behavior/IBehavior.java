package cgx.core.behavior;

@FunctionalInterface
public interface IBehavior<C> {

    void behave(C ctx);
}

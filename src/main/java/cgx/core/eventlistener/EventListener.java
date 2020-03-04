package cgx.core.eventlistener;

@FunctionalInterface
public interface EventListener<E> {

    void onEvent(E event);
}

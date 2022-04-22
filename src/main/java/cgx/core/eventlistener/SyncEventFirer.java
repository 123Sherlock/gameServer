package cgx.core.eventlistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 同步事件发射器，事件发出后立即执行监听器
 */
public class SyncEventFirer<E> {

    /**
     * 发出同步事件
     */
    public void fire(E event) {
        for (EventListener<E> listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    @Autowired(required = false)
    private Set<EventListener<E>> listeners;
}

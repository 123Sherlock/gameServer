package cgx.core.behavior;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBehaviorMap<K, B extends IBehavior> {

    public B getBehavior(K key) {
        return _behaviorMap.get(key);
    }

    @PostConstruct
    public void init() {
        ParameterizedType superParamType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<B> behaviorClass = (Class<B>) superParamType.getActualTypeArguments()[0];

        for (B behavior : _applicationContext.getBeansOfType(behaviorClass).values()) {
            K key = getKey(behavior);
            B oldBehavior = _behaviorMap.put(key, behavior);
            if (oldBehavior != null) {
                throw new BehaviorNameException(oldBehavior.getClass().getSimpleName());
            }
        }
    }

    protected abstract K getKey(B behavior);

    protected Map<K, B> _behaviorMap = new HashMap<>();

    @Autowired
    private ApplicationContext _applicationContext;
}

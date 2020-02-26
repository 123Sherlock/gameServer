package cgx.core.spring;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * SpringBean收集辅助类
 */
@Component
public class BeanResultMaker {

  public <K, T> ImmutableMap<K, T> makeMap(Collection<T> beans, Function<T, K> keyMapper) {
    Map<K, T> resultMap = new HashMap<>(beans.size());

    for (T bean : beans) {
      K id = keyMapper.apply(bean);
      if (id == null) {
        throw new NullPointerException("键值不能为null！");
      }

      T old = resultMap.put(id, bean);
      if (old != null) {
        throw new RuntimeException("键值重复注册："
            + id + ", " + old.getClass() + " <-> " + bean.getClass());
      }
    }

    return ImmutableMap.copyOf(resultMap);
  }
}

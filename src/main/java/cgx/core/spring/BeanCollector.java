package cgx.core.spring;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Spring工具类，封装一些Bean收集方法
 */
@Component
public class BeanCollector {

  @Autowired
  private ApplicationContext _applicationContext;

  @Autowired
  private BeanResultMaker _beanResultMaker;

  public <T> List<T> collectBeanList(Class<T> beanType) {
    return ImmutableList.copyOf(_applicationContext.getBeansOfType(beanType).values());
  }

  public <K, T> Map<K, T> collectBeanMap(Class<T> beanType, Function<T, K> keyMapper) {
    Collection<T> beans = _applicationContext.getBeansOfType(beanType).values();
    return _beanResultMaker.makeMap(beans, keyMapper);
  }

  public <K, T> Map<K, T> collectBeanMap(Class<T> beanType,
    Class<? extends Annotation> anno, Function<T, K> keyMapper) {
    return _beanResultMaker.makeMap(_applicationContext
        .getBeansOfType(beanType).values().stream()
        .filter(b -> b.getClass().getAnnotation(anno) != null)
        .collect(Collectors.toList()), keyMapper);
  }
}

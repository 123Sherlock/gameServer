package cgx.core.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DbServer {

    private static final int defaultCacheSize = 1000;

    private final Map<String, LoadingCache<Long, BaseDb>> loadingCacheMap = new ConcurrentHashMap<>(10);

    public LoadingCache<Long, BaseDb> getLoadingCache(BaseDb dbClass) {
        String dbName = dbClass.getClass().getSimpleName();
        return loadingCacheMap.computeIfAbsent(dbName, createLoadingCache(dbClass));
    }

    public <T extends BaseDb> LoadingCache createLoadingCache(T dbClass) {
        return CacheBuilder.newBuilder()
                .maximumSize(defaultCacheSize)
                .concurrencyLevel(8) // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .initialCapacity(100) // 设置缓存容器的初始容量为10
                .recordStats() // 设置要统计缓存的命中率
                .build(new DbServer.BaseDbCacheLoader<>(dbClass));
    }

    private static class BaseDbCacheLoader<T extends BaseDb> extends CacheLoader<Long, T> {
        private final T dbClass;

        public BaseDbCacheLoader(T dbClass) {
            this.dbClass = dbClass;
        }

        @Override
        public T load(Long id) {
            String dbName = dbClass.getClass().getSimpleName();
            String sql = String.format("SELECT `data` FROM `%s` WHERE `id` = %s", dbName, id);
            List<String> result = MysqlService.queryList(sql);
            if (result.isEmpty()) {
                return null;
            }

            JSONObject jsonObj = JSON.parseObject(result.get(0));
            try {
                BeanUtils.populate(dbClass, jsonObj.getInnerMap());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dbClass;
        }
    }
}

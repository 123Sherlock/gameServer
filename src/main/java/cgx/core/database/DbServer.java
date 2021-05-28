package cgx.core.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DbServer {

    private static final int defaultCacheSize = 1000;

    private final Map<String, LoadingCache<Long, BaseDb>> loadingCacheMap = new ConcurrentHashMap<>(10);

    public <T extends BaseDb> LoadingCache<Long, T> getLoadingCache(Class<T> dbClass) {
        String dbName = dbClass.getSimpleName();
        return (LoadingCache<Long, T>) loadingCacheMap.computeIfAbsent(dbName, k -> createLoadingCache((Class<BaseDb>) dbClass));
    }

    public <T extends BaseDb> LoadingCache<Long, T> createLoadingCache(Class<T> dbClass) {
        return CacheBuilder.newBuilder()
                .maximumSize(defaultCacheSize)
                .concurrencyLevel(8) // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .initialCapacity(100) // 设置缓存容器的初始容量为10
                .recordStats() // 设置要统计缓存的命中率
                .build(new DbServer.BaseDbCacheLoader<>(dbClass));
    }

    private static class BaseDbCacheLoader<T extends BaseDb> extends CacheLoader<Long, T> {

        @Override
        public T load(Long id) {
            String dbName = _dbClass.getSimpleName().toLowerCase(Locale.ENGLISH);
            String sql = String.format("SELECT `data` FROM `%s` WHERE `id` = %s", dbName, id);
            List<String> result = MysqlService.queryList(sql);
            if (result.isEmpty()) {
                return null;
            }

            try {
                T newDb = _dbClass.getConstructor().newInstance();
                JSONObject jsonObj = JSON.parseObject(result.get(0));
                BeanUtils.populate(newDb, jsonObj.getInnerMap());
                return newDb;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        BaseDbCacheLoader(Class<T> dbClass) {
            _dbClass = dbClass;
        }

        private final Class<T> _dbClass;
    }
}

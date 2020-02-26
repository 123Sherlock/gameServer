package cgx.core.database;

import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbGetOrCreator {

    @Autowired
    private DbServer dbServer;

    public <T extends BaseDb> T getDb(T dbClass, Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        try {
            LoadingCache<Long, BaseDb> loadingCache = dbServer.getLoadingCache(dbClass);
            return (T) loadingCache.get(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

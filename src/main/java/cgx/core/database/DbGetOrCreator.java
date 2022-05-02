package cgx.core.database;

import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DbGetOrCreator {

    public <T extends BaseDb> T getDb(Class<T> dbClass, Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        try {
            LoadingCache<Long, T> loadingCache = dbServer.getLoadingCache(dbClass);
            return loadingCache.get(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T extends BaseDb> List<T> getDbList(Class<T> dbClass, List<Long> idList) {
        return idList.stream()
            .map(id -> getDb(dbClass, id))
            .collect(Collectors.toList());
    }

    public <T extends BaseDb> List<T> getAllDbList(Class<T> dbClass) {
        return dbServer.selectAllByTable(dbClass);
    }

    @Autowired
    private DbServer dbServer;
}

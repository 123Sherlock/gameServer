package cgx.logic.rank;

import cgx.core.database.DbGetOrCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RankDbGetter {

    public <T extends RankDb> List<T> getRankDbList(Class<T> rankType) {
        return dbGetOrCreator.getAllDbList(rankType);
    }

    @Autowired
    private DbGetOrCreator dbGetOrCreator;
}

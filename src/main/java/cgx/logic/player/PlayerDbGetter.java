package cgx.logic.player;

import cgx.core.database.DbGetOrCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlayerDbGetter {

    public PlayerDb get(Long playerId) {
        return dbGetOrCreator.getDb(PlayerDb.class, playerId);
    }

    @Autowired
    private DbGetOrCreator dbGetOrCreator;
}

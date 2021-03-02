package cgx.logic.bag;

import cgx.core.database.DbGetOrCreator;
import cgx.logic.player.PlayerDbGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BagDbGetter {

    public BagDb get(Long bagId) {
        return dbGetOrCreator.getDb(BagDb.class, bagId);
    }

    public BagDb getByPlayerId(Long playerId) {
        return get(playerDbGetter.get(playerId).getBagDbId());
    }

    @Autowired
    private DbGetOrCreator dbGetOrCreator;

    @Autowired
    private PlayerDbGetter playerDbGetter;
}

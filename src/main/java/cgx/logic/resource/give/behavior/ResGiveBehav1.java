package cgx.logic.resource.give.behavior;

import cgx.logic.player.PlayerDb;
import cgx.logic.player.PlayerDbGetter;
import cgx.logic.resource.ResourceWrap;
import cgx.logic.resource.give.ResGiveBehavior;
import cgx.logic.resource.give.ResGiveContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 只存数字的资源
 */
@Component
public class ResGiveBehav1 implements ResGiveBehavior {

    @Override
    public void behave(ResGiveContext ctx) {
        ResourceWrap resource = ctx.getResource();
        Integer configId = resource.getConfigId();
        long amount = resource.getAmount();

        PlayerDb playerDb = playerDbGetter.get(ctx.getPlayerId());
        playerDb.getResourceMap().merge(configId, amount, (k, v) -> v + amount);
    }

    @Autowired
    private PlayerDbGetter playerDbGetter;
}

package cgx.logic.resource.give.behavior;

import cgx.core.dataconfig.DataConfigManager;
import cgx.core.time.TimeTool;
import cgx.logic.bag.BagDb;
import cgx.logic.bag.BagDbGetter;
import cgx.logic.bag.BagGridDb;
import cgx.logic.bag.BagManager;
import cgx.logic.item.BagItemCfg;
import cgx.logic.item.BagItemDb;
import cgx.logic.item.BagItemManager;
import cgx.logic.resource.ResourceWrap;
import cgx.logic.resource.give.ResGiveBehavior;
import cgx.logic.resource.give.ResGiveContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 向道具背包添加资源
 */
@Component
public class ResGiveBehav2 implements ResGiveBehavior {

    @Override
    public void behave(ResGiveContext ctx) {
        BagDb bagDb = bagDbGetter.getByPlayerId(ctx.getPlayerId());
        ResourceWrap resource = ctx.getResource();
        Integer configId = resource.getConfigId();

        BagItemCfg bagItemCfg = dataConfigManager.get(BagItemCfg.class, configId);
        int pileLimit = bagItemCfg.getPileLimit();
        int amount = (int) resource.getAmount();
        Map<Integer, BagGridDb> gridMap = bagDb.getGridMap();

        // 寻找能堆叠的格子
        Optional<BagItemDb> pilableItemDbOpt = gridMap.values().stream()
            .map(BagGridDb::getBagItemDb)
            .filter((i) -> Objects.equals(i.getConfigId(), configId))
            .filter((i) -> i.getExtra() == null)
            .filter(bagItemManager::isPermenent)
            .filter((i) -> i.getAmount() + amount <= pileLimit)
            .findFirst();

        if (pilableItemDbOpt.isPresent()) {
            BagItemDb pilableItemDb = pilableItemDbOpt.get();
            pilableItemDb.setAmount(pilableItemDb.getAmount() + amount);
        } else {
            Integer index = bagManager.findFirstEmptyIndex(bagDb);
            BagItemDb bagItemDb = newBagItemDb(index, configId, amount, resource.getDuration());

            BagGridDb bagGridDb = new BagGridDb();
            bagGridDb.setIndex(index);
            bagGridDb.setBagItemDb(bagItemDb);
            gridMap.put(index, bagGridDb);
        }

        bagDb.save();
        ctx.setGivenResource(resource);
    }

    private BagItemDb newBagItemDb(Integer index, Integer configId, int amount, long duration) {
        BagItemDb bagItemDb = new BagItemDb();
        bagItemDb.setIndex(index);
        bagItemDb.setConfigId(configId);
        bagItemDb.setAmount(amount);

        long now = timeTool.curMilli();
        bagItemDb.setGainTime(now);
        bagItemDb.setExpiredTime(now + duration);
        return bagItemDb;
    }

    @Autowired
    private BagDbGetter bagDbGetter;

    @Autowired
    private DataConfigManager dataConfigManager;

    @Autowired
    private BagItemManager bagItemManager;

    @Autowired
    private BagManager bagManager;

    @Autowired
    private TimeTool timeTool;
}

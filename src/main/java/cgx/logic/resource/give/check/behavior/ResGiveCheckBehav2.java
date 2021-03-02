package cgx.logic.resource.give.check.behavior;

import cgx.core.dataconfig.DataConfigManager;
import cgx.logic.bag.BagDb;
import cgx.logic.bag.BagDbGetter;
import cgx.logic.bag.BagGridDb;
import cgx.logic.bag.BagManager;
import cgx.logic.define.ErrorCodeDefine;
import cgx.logic.item.BagItemCfg;
import cgx.logic.item.BagItemDb;
import cgx.logic.item.BagItemManager;
import cgx.logic.resource.ResourceWrap;
import cgx.logic.resource.give.check.ResCheckResultCode;
import cgx.logic.resource.give.check.ResGiveCheckBehavior;
import cgx.logic.resource.give.check.ResGiveCheckContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 背包道具
 */
@Component
public class ResGiveCheckBehav2 implements ResGiveCheckBehavior {

    @Override
    public void behave(ResGiveCheckContext ctx) {
        BagDb bagDb = bagDbGetter.getByPlayerId(ctx.getPlayerId());
        int emptyGrids = bagManager.getEmptyGrids(bagDb);
        List<ResCheckResultCode> resultCodes = ctx.getResultCodes();

        for (ResourceWrap resource : ctx.getResources()) {
            long addAmount = resource.getAmount();
            if (addAmount <= 0) {
                resultCodes.add(new ResCheckResultCode(resource, ErrorCodeDefine.E3_002));
                continue;
            }

            Integer configId = resource.getConfigId();
            BagItemCfg bagItemCfg = dataConfigManager.get(BagItemCfg.class, configId);
            if (bagItemCfg == null) {
                resultCodes.add(new ResCheckResultCode(resource, ErrorCodeDefine.E1_002));
                continue;
            }

            int pileLimit = bagItemCfg.getPileLimit();
            List<ResourceWrap> givableResources = ctx.getGivableResources();

            // 如果给与的资源是永久的，尝试堆叠
            if (resource.isPermenent()) {
                addAmount = tryPile(resource, bagDb, configId, pileLimit, addAmount, givableResources);
            }

            // 尝试堆叠完后还有剩余
            if (addAmount > 0) {
                emptyGrids = handleUnpilable(addAmount, pileLimit, emptyGrids, resource, resultCodes, givableResources);
            }
        }
    }

    /**
     * 尝试堆叠
     * @return 堆叠完后剩余的数量
     */
    private long tryPile(ResourceWrap resource, BagDb bagDb, Integer configId,
        int pileLimit, long addAmount, List<ResourceWrap> givableResources) {
        // 所有可堆叠的格子
        List<BagItemDb> bagItemDbs = bagDb.getGridMap().values().stream()
            .map(BagGridDb::getBagItemDb)
            .filter((i) -> Objects.equals(i.getConfigId(), configId))
            .filter((i) -> i.getExtra() == null) // 暂定无额外信息的才可堆叠
            .filter(bagItemManager::isPermenent) // 暂定永久的才可堆叠
            .filter((i) -> i.getAmount() < pileLimit)
            .collect(Collectors.toList());

        // 开始堆叠
        long remainAmount = addAmount;
        for (BagItemDb itemDb : bagItemDbs) {
            int addableAmount = pileLimit - itemDb.getAmount();
            long actualAddAmount = Math.min(remainAmount, addableAmount);
            givableResources.add(newResourceWrap(resource, actualAddAmount));

            remainAmount -= actualAddAmount;
            if (remainAmount == 0) {
                break;
            }
        }
        return remainAmount;
    }

    /**
     * 处理尝试堆叠后还有剩余的部分
     * @return 预占用格子后剩余的空格数
     */
    private int handleUnpilable(long addAmount, int pileLimit, int emptyGrids,
        ResourceWrap resource, List<ResCheckResultCode> resultCodes, List<ResourceWrap> givableResources) {
        int remainEmptyGrids = emptyGrids;
        long pileLimitGrids = addAmount / pileLimit; // 需要继续叠满的格子数
        long remainAmount = addAmount % pileLimit; // 叠不满的剩余物品数量
        long needGrids = pileLimitGrids + (remainAmount > 0 ? 1 : 0);
        if (needGrids > remainEmptyGrids) {
            resultCodes.add(new ResCheckResultCode(resource, ErrorCodeDefine.E3_003));
        }

        for (long i = 0; i < pileLimitGrids; i++) {
            givableResources.add(newResourceWrap(resource, pileLimit));
        }
        if (remainAmount > 0) {
            givableResources.add(newResourceWrap(resource, remainAmount));
        }

        // 预占用现在的空格
        remainEmptyGrids -= needGrids;
        return remainEmptyGrids;
    }

    private ResourceWrap newResourceWrap(ResourceWrap resource, long amount) {
        return new ResourceWrap(resource.getType(), resource.getSubType(),
            resource.getConfigId(), amount, resource.getDuration());
    }

    @Autowired
    private BagDbGetter bagDbGetter;

    @Autowired
    private DataConfigManager dataConfigManager;

    @Autowired
    private BagItemManager bagItemManager;

    @Autowired
    private BagManager bagManager;
}

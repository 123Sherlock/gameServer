package cgx.logic.bag;

import cgx.logic.define.ErrorCodeDefine;
import cgx.logic.exception.LogicException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.IntStream;

@Component
public class BagManager {

    /**
     * 获取道具背包剩余空格数
     */
    public int getEmptyGrids(BagDb bagDb) {
        return bagDb.getGridAmount() - bagDb.getGridMap().size();
    }

    /**
     * 查找道具背包第一个空格的索引
     */
    public int findFirstEmptyIndex(BagDb bagDb) {
        Map<Integer, BagGridDb> gridMap = bagDb.getGridMap();
        return IntStream.range(0, bagDb.getGridAmount())
            .filter(i -> !gridMap.containsKey(i))
            .findFirst()
            .orElseThrow(() -> new LogicException(ErrorCodeDefine.E3_004));
    }
}

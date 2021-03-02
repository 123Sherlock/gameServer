package cgx.logic.bag;

import cgx.core.database.BaseDb;

import java.util.Map;

/**
 * 玩家道具背包
 */
public class BagDb extends BaseDb {

    private Long _playerId;

    /**
     * 可用的格子总数
     */
    private int _gridAmount;

    /**
     * 背包格子
     * key:格子索引 {@link BagGridDb#getIndex}
     * value:格子详情
     */
    private Map<Integer, BagGridDb> _gridMap;

    public Long getPlayerId() {
        return _playerId;
    }

    public void setPlayerId(Long playerId) {
        _playerId = playerId;
    }

    public int getGridAmount() {
        return _gridAmount;
    }

    public void setGridAmount(int gridAmount) {
        _gridAmount = gridAmount;
    }

    public Map<Integer, BagGridDb> getGridMap() {
        return _gridMap;
    }

    public void setGridMap(Map<Integer, BagGridDb> gridMap) {
        _gridMap = gridMap;
    }
}

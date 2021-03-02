package cgx.logic.bag;

import cgx.logic.item.BagItemDb;

/**
 * 背包中的一个格子
 */
public class BagGridDb {

    /**
     * 格子索引
     */
    private Integer _index;

    /**
     * 存放的道具
     */
    private BagItemDb _bagItemDb;

    /**
     * 格子的获得/解锁时间
     */
//    private long _gainTime;

    public Integer getIndex() {
        return _index;
    }

    public void setIndex(Integer index) {
        _index = index;
    }

    public BagItemDb getBagItemDb() {
        return _bagItemDb;
    }

    public void setBagItemDb(BagItemDb bagItemDb) {
        _bagItemDb = bagItemDb;
    }
}

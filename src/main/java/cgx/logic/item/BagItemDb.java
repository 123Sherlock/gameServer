package cgx.logic.item;

import cgx.logic.bag.BagGridDb;
import cgx.logic.resource.ResourceExtra;

/**
 * 背包中的一个道具
 */
public class BagItemDb {

    /**
     * 在背包中的索引
     * @see BagGridDb#getIndex
     */
    private Integer _index;

    /**
     * 配置id
     * @see BagItemCfg#getId
     */
    private Integer _configId;

    /**
     * 数量
     */
    private int _amount;

    /**
     * 获得时间
     */
    private long _gainTime;

    /**
     * 过期时间（负数表示永久）
     */
    private long _expiredTime;

    /**
     * 额外信息，根据不同需求使用不同的子类扩展
     */
    private ResourceExtra _extra;

    public Integer getIndex() {
        return _index;
    }

    public void setIndex(Integer index) {
        _index = index;
    }

    public Integer getConfigId() {
        return _configId;
    }

    public void setConfigId(Integer configId) {
        _configId = configId;
    }

    public int getAmount() {
        return _amount;
    }

    public void setAmount(int amount) {
        _amount = amount;
    }

    public long getGainTime() {
        return _gainTime;
    }

    public void setGainTime(long gainTime) {
        _gainTime = gainTime;
    }

    public long getExpiredTime() {
        return _expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        _expiredTime = expiredTime;
    }

    public ResourceExtra getExtra() {
        return _extra;
    }

    public void setExtra(ResourceExtra extra) {
        _extra = extra;
    }
}

package cgx.logic.resource;

/**
 * 代表一份资源
 */
public class ResourceWrap {

    /**
     * 大类
     */
    private Integer _type;

    /**
     * 小类
     */
    private Integer _subType;

    /**
     * 配置id
     */
    private Integer _configId;

    /**
     * 数量
     */
    private long _amount;

    /**
     * 有效时长
     */
    private long _duration;

    /**
     * 是否永久有效
     */
    public boolean isPermenent() {
        return _duration < 0;
    }

    public ResourceWrap(Integer type, Integer subType, Integer configId, long amount, long duration) {
        _type = type;
        _subType = subType;
        _configId = configId;
        _amount = amount;
        _duration = duration;
    }

    public Integer getType() {
        return _type;
    }

    public void setType(Integer type) {
        _type = type;
    }

    public Integer getSubType() {
        return _subType;
    }

    public void setSubType(Integer subType) {
        _subType = subType;
    }

    public Integer getConfigId() {
        return _configId;
    }

    public void setConfigId(Integer configId) {
        _configId = configId;
    }

    public long getAmount() {
        return _amount;
    }

    public void setAmount(long amount) {
        _amount = amount;
    }

    public long getDuration() {
        return _duration;
    }

    public void setDuration(long duration) {
        _duration = duration;
    }
}

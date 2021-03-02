package cgx.logic.player;

import cgx.core.database.BaseDb;
import cgx.logic.bag.BagDb;

import java.util.Map;

public class PlayerDb extends BaseDb {

    /**
     * 昵称
     */
    private String _name;

    /**
     * 等级
     */
    private int _level;

    /**
     * 只需存数字的资源（key:资源id，value:数量）
     */
    private Map<Integer, Long> _resourceMap;

    /**
     * 道具背包
     * @see BagDb#getId
     */
    private Long _bagDbId;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public int getLevel() {
        return _level;
    }

    public void setLevel(int level) {
        _level = level;
    }

    public Map<Integer, Long> getResourceMap() {
        return _resourceMap;
    }

    public void setResourceMap(Map<Integer, Long> resourceMap) {
        _resourceMap = resourceMap;
    }

    public Long getBagDbId() {
        return _bagDbId;
    }

    public void setBagDbId(Long bagDbId) {
        _bagDbId = bagDbId;
    }
}

package cgx.logic.rank;

import cgx.core.dataconfig.DataConfig;

/**
 * 排行榜配置
 */
public class RankCfg extends DataConfig {

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLeastScore() {
        return leastScore;
    }

    public void setLeastScore(int leastScore) {
        this.leastScore = leastScore;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    /**
     * 上榜人数
     */
    private int size;

    /**
     * 上榜最低条件
     */
    private int leastScore;

    /**
     * 刷新间隔（单位分钟，0表示实时更新）
     */
    private int refreshInterval;
}

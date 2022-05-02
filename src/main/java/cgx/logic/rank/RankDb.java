package cgx.logic.rank;

import cgx.core.database.BaseDb;

/**
 * 排行榜玩家数据父DB，各排行榜分别扩展
 */
public class RankDb extends BaseDb {

    private Long _playerId;

    private int _score;

    private long _time;

    public Long getPlayerId() {
        return _playerId;
    }

    public void setPlayerId(Long playerId) {
        _playerId = playerId;
    }

    public int getScore() {
        return _score;
    }

    public void setScore(int score) {
        _score = score;
    }

    public long getTime() {
        return _time;
    }

    public void setTime(long time) {
        _time = time;
    }
}

package cgx.logic.activity.time;

import cgx.logic.activity.ActivityCfg;

import java.time.LocalDateTime;

public class ActivityTimeContext {

    /**
     * @see ActivityCfg#getBeginTimeCfg
     */
    private final String _beginTimeCfg;

    private LocalDateTime _beginTime;

    public ActivityTimeContext(String beginTimeCfg) {
        _beginTimeCfg = beginTimeCfg;
    }

    public String getBeginTimeCfg() {
        return _beginTimeCfg;
    }

    public LocalDateTime getBeginTime() {
        return _beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        _beginTime = beginTime;
    }
}

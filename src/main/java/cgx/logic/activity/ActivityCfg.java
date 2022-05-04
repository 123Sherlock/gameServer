package cgx.logic.activity;

import cgx.core.dataconfig.DataConfig;
import cgx.logic.activity.time.ActivityTimeBehavior;
import cgx.logic.activity.time.ActivityTimeContext;

/**
 * 活动总配置表
 */
public class ActivityCfg extends DataConfig {

    private ActivityId activityId;

    /**
     * @see ActivityTimeBehavior
     */
    private int timeType;

    /**
     * @see ActivityTimeContext#getBeginTimeCfg
     */
    private String beginTimeCfg;

    /**
     * 活动持续时长（分）
     */
    private int durationMinutes;

    @Override
    public void afterOneRowSet() {
        activityId = ActivityId.getById(getId());
    }

    public ActivityId getActivityId() {
        return activityId;
    }

    public void setActivityId(ActivityId activityId) {
        this.activityId = activityId;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public String getBeginTimeCfg() {
        return beginTimeCfg;
    }

    public void setBeginTimeCfg(String beginTimeCfg) {
        this.beginTimeCfg = beginTimeCfg;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
}

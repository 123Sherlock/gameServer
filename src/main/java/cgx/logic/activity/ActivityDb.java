package cgx.logic.activity;

import cgx.core.database.BaseDb;

public class ActivityDb extends BaseDb {

    /**
     * @see ActivityCfg#getId
     */
    private int _activityId;

    private String _beginTime;

    private String _endTime;

    /**
     * 活动是否进行中，用于重启服务器后恢复活动
     */
    private boolean _running;

    /**
     * 活动中的临时数据，用于活动进行中重启服务器后恢复活动
     */
    private String _temporaryData;

    public int getActivityId() {
        return _activityId;
    }

    public void setActivityId(int activityId) {
        _activityId = activityId;
    }

    public String getBeginTime() {
        return _beginTime;
    }

    public void setBeginTime(String beginTime) {
        _beginTime = beginTime;
    }

    public String getEndTime() {
        return _endTime;
    }

    public void setEndTime(String endTime) {
        _endTime = endTime;
    }

    public boolean isRunning() {
        return _running;
    }

    public void setRunning(boolean running) {
        _running = running;
    }

    public String getTemporaryData() {
        return _temporaryData;
    }

    public void setTemporaryData(String temporaryData) {
        _temporaryData = temporaryData;
    }
}

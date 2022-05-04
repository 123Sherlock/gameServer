package cgx.logic.activity;

import cgx.core.dataconfig.DataConfigManager;
import cgx.core.time.TimeTool;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * 全服活动处理器父类
 */
public abstract class AbstractActivityProcessor {

    protected LocalDateTime beginTime;

    protected LocalDateTime endTime;

    /**
     * 活动开始
     * 子类覆盖时要先调用super()
     */
    public void begin() {
        ActivityDb activityDb = getActivityDb();
        activityDb.setActivityId(getActvityId().getId());
        activityDb.setBeginTime(timeTool.toCommonTimeStr(beginTime));
        activityDb.setEndTime(timeTool.toCommonTimeStr(endTime));
        activityDb.setRunning(true);
        activityDb.save();
    }

    /**
     * 活动结束
     * 子类覆盖时要先调用super()
     */
    public void end() {
        ActivityDb activityDb = getActivityDb();
        activityDb.setRunning(false);
        activityDb.save();

        ActivityCfg config = getActivityCfg();
        LocalDateTime nextBeginTime = activityManager.getNextBeginTime(config);
        LocalDateTime nextEndTime = activityManager.calculateEndTime(config, nextBeginTime);
        beginTime = nextBeginTime;
        endTime = nextEndTime;
    }

    /**
     * 活动是否进行中
     */
    public boolean isRunning(LocalDateTime now) {
        return !beginTime.isAfter(now) && !endTime.isBefore(now);
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    protected ActivityCfg getActivityCfg() {
        return dataConfigManager.get(ActivityCfg.class, getActvityId().getId());
    }

    protected ActivityDb getActivityDb() {
        return activityDbGetter.getOrCreate(getActvityId().getId());
    }

    /**
     * 与活动配置表对应的id
     */
    protected abstract ActivityId getActvityId();

    /**
     * 对活动中的临时数据编码，用于重启服务器后恢复
     * @see #recover
     * @return {@link ActivityDb#setTemporaryData}
     */
    protected abstract String encodeTemporaryData();

    /**
     * 活动进行中重启服务器时恢复临时数据
     * @see #encodeTemporaryData
     * @param temporaryData {@link ActivityDb#getTemporaryData}
     */
    protected abstract void recover(String temporaryData);

    @Autowired
    protected DataConfigManager dataConfigManager;

    @Autowired
    protected TimeTool timeTool;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private ActivityDbGetter activityDbGetter;
}

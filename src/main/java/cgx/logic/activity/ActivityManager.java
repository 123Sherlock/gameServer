package cgx.logic.activity;

import cgx.core.dataconfig.DataConfigManager;
import cgx.core.spring.BeanCollector;
import cgx.core.time.TimeTool;
import cgx.logic.activity.time.ActivityTimeBehavMap;
import cgx.logic.activity.time.ActivityTimeBehavior;
import cgx.logic.activity.time.ActivityTimeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ActivityManager {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);

    private Map<ActivityId, AbstractActivityProcessor> processorMap;

    public void init() {
        processorMap = beanCollector.collectBeanMap(AbstractActivityProcessor.class, AbstractActivityProcessor::getActvityId);
        LocalDateTime now = timeTool.now();
        List<Integer> recoverIdList = new ArrayList<>();

        // 先处理数据库中开始后又未正常结束的活动
        for (ActivityDb activityDb : activityDbGetter.getAll()) {
            if (!activityDb.isRunning()) {
                continue;
            }

            // 恢复活动中的临时数据
            AbstractActivityProcessor processor = processorMap.get(ActivityId.getById(activityDb.getActivityId()));
            processor.setBeginTime(timeTool.parseCommonTime(activityDb.getBeginTime()));
            LocalDateTime endTime = timeTool.parseCommonTime(activityDb.getEndTime());
            processor.setEndTime(endTime);

            processor.recover(activityDb.getTemporaryData());
            recoverIdList.add(activityDb.getActivityId());

            if (endTime.isAfter(now)) {
                continue;
            }
            // 之前进行中的活动未正常结束就过期了，立即进行结算
            processor.end();
        }

        // 不需要从数据库恢复的活动根据配置表计算下次的时间
        for (ActivityCfg config : dataConfigManager.getAll(ActivityCfg.class)) {
            if (recoverIdList.contains(config.getId())) {
                continue;
            }

            LocalDateTime nextBeginTime = getNextBeginTime(config);
            LocalDateTime endTime = calculateEndTime(config, nextBeginTime);

            AbstractActivityProcessor processor = processorMap.get(config.getActivityId());
            if (processor == null) {
                System.err.println(String.format("活动配置表找不到对应的处理器，id = %s", config.getId()));
                continue;
            }

            processor.setBeginTime(nextBeginTime);
            processor.setEndTime(endTime);
        }

        // 每秒检查一次活动开启和关闭
        executor.scheduleAtFixedRate(new ActivityCheckExecutor(), 0, 1, TimeUnit.SECONDS);
        // 每分钟进行一次活动数据持久化
        executor.scheduleAtFixedRate(new ActivityPersistentExecutor(), 0, 1, TimeUnit.MINUTES);
    }

    LocalDateTime getNextBeginTime(ActivityCfg config) {
        ActivityTimeBehavior behavior = activityTimeBehavMap.getBehavior(config.getTimeType());
        ActivityTimeContext ctx = new ActivityTimeContext(config.getBeginTimeCfg());
        behavior.behave(ctx);
        return ctx.getBeginTime();
    }

    LocalDateTime calculateEndTime(ActivityCfg config, LocalDateTime beginTime) {
        return beginTime.plusMinutes(config.getDurationMinutes());
    }

    Map<ActivityId, AbstractActivityProcessor> getProcessorMap() {
        return processorMap;
    }

    @Autowired
    private ActivityDbGetter activityDbGetter;

    @Autowired
    private TimeTool timeTool;

    @Autowired
    private DataConfigManager dataConfigManager;

    @Autowired
    private ActivityTimeBehavMap activityTimeBehavMap;

    @Autowired
    private BeanCollector beanCollector;
}

package cgx.logic.activity;

import cgx.core.time.TimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 持久化进行中的活动的临时数据
 */
@Component
public class ActivityPersistentExecutor implements Runnable {

    @Override
    public void run() {
        LocalDateTime now = timeTool.now();
        for (AbstractActivityProcessor processor : activityManager.getProcessorMap().values()) {
            if (!processor.isRunning(now)) {
                continue;
            }

            ActivityDb activityDb = processor.getActivityDb();
            String temporaryData = processor.encodeTemporaryData();
            activityDb.setTemporaryData(temporaryData);
            activityDb.save();
        }
    }

    @Autowired
    private TimeTool timeTool;

    @Autowired
    private ActivityManager activityManager;
}

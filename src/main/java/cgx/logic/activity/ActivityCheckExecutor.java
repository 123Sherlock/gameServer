package cgx.logic.activity;

import cgx.core.time.TimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 检查活动开始和结束
 */
@Component
public class ActivityCheckExecutor implements Runnable {

    @Override
    public void run() {
        LocalDateTime now = timeTool.now();
        // 为了避免活动的开始和结束逻辑执行太久影响了其他活动的时间判断，先暂存再执行
        List<AbstractActivityProcessor> beginList = new ArrayList<>();
        List<AbstractActivityProcessor> endList = new ArrayList<>();

        for (AbstractActivityProcessor processor : activityManager.getProcessorMap().values()) {
            if (processor.isRunning(now)) {
                if (processor.getEndTime().isEqual(now)) {
                    endList.add(processor);
                }
                continue;
            }
            if (processor.getBeginTime().isEqual(now)) {
                beginList.add(processor);
            }
        }

        for (AbstractActivityProcessor processor : beginList) {
            processor.begin();
        }
        for (AbstractActivityProcessor processor : endList) {
            processor.end();
        }
    }

    @Autowired
    private TimeTool timeTool;

    @Autowired
    private ActivityManager activityManager;
}

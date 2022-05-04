package cgx.logic.activity.time.behavior;

import cgx.core.time.TimeTool;
import cgx.logic.activity.time.ActivityTimeBehavior;
import cgx.logic.activity.time.ActivityTimeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 以开始时间为起点，间隔时间固定（分）
 * 例：2022-05-01 00:00:00|10080
 */
@Component
public class ActivityTimeBehav1 implements ActivityTimeBehavior {

    @Override
    public void behave(ActivityTimeContext ctx) {
        String[] split = ctx.getBeginTimeCfg().split("\\|");
        LocalDateTime beginTime = timeTool.parseCommonTime(split[0]);
        int intervalMinutes = Integer.parseInt(split[1]);

        LocalDateTime now = timeTool.now();
        while (beginTime.isBefore(now)) {
            beginTime = beginTime.plusMinutes(intervalMinutes);
        }
        ctx.setBeginTime(beginTime);
    }

    @Autowired
    private TimeTool timeTool;
}

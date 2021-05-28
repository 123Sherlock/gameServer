package cgx.logic.resource.give.check.behavior;

import cgx.logic.define.ErrorCodeDefine;
import cgx.logic.resource.give.check.ResCheckResultCode;
import cgx.logic.resource.give.check.ResGiveCheckBehavior;
import cgx.logic.resource.give.check.ResGiveCheckContext;
import org.springframework.stereotype.Component;

/**
 * 只存数字的资源
 */
@Component
public class ResGiveCheckBehav1 implements ResGiveCheckBehavior {

    @Override
    public void behave(ResGiveCheckContext ctx) {
        ctx.getResources().stream()
            .map(r -> new ResCheckResultCode(r, ErrorCodeDefine.SUCCESS))
            .forEach(ctx.getResultCodes()::add);
        ctx.getGivableResources().addAll(ctx.getResources());
    }
}

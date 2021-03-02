package cgx.logic.resource.give.check;

import cgx.logic.resource.ResourceWrap;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * 给与资源检查结果
 */
public class ResGiveCheckResult {

    /**
     * 检查结果码
     */
    private final List<ResCheckResultCode> _resultCodeList;

    /**
     * 实际可以给予的资源
     */
    private final List<ResourceWrap> _givableResList;

    public ResGiveCheckResult(List<ResCheckResultCode> resultCodeList, List<ResourceWrap> givableResList) {
        _resultCodeList = ImmutableList.copyOf(resultCodeList);
        _givableResList = ImmutableList.copyOf(givableResList);
    }

    public List<ResCheckResultCode> getResultCodeList() {
        return _resultCodeList;
    }

    public List<ResourceWrap> getGivableResList() {
        return _givableResList;
    }
}

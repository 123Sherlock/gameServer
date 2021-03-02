package cgx.logic.resource.give;

import cgx.logic.resource.ResourceWrap;
import cgx.logic.resource.give.check.ResCheckResultCode;

import java.util.List;

/**
 * 给与资源结果
 */
public class ResGiveResult {

    /**
     * 每种资源给与的结果码
     */
    private final List<ResCheckResultCode> _resultCodeList;

    /**
     * 成功给与的资源详情
     */
    private final List<ResourceWrap> _givenResourceList;

    public ResGiveResult(List<ResCheckResultCode> resultCodeList, List<ResourceWrap> givenResourceList) {
        _resultCodeList = resultCodeList;
        _givenResourceList = givenResourceList;
    }

    public List<ResCheckResultCode> getResultCodeList() {
        return _resultCodeList;
    }

    public List<ResourceWrap> getGivenResourceList() {
        return _givenResourceList;
    }
}

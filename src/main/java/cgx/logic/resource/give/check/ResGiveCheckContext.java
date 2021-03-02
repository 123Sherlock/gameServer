package cgx.logic.resource.give.check;

import cgx.logic.resource.ResourceTrack;
import cgx.logic.resource.ResourceWrap;

import java.util.ArrayList;
import java.util.List;

/**
 * 给与资源检查上下文
 */
public class ResGiveCheckContext {

    private final Long _playerId;

    /**
     * 资源内容
     */
    private final List<ResourceWrap> _resources;

    /**
     * 资源来源
     */
    private final ResourceTrack _resourceTrack;

    /**
     * 检查结果
     */
    private final List<ResCheckResultCode> _resultCodes;

    /**
     * 实际可给予的资源
     */
    private final List<ResourceWrap> _givableResources;

    public ResGiveCheckContext(Long playerId, List<ResourceWrap> resources, ResourceTrack resourceTrack) {
        _playerId = playerId;
        _resources = resources;
        _resourceTrack = resourceTrack;
        _resultCodes = new ArrayList<>();
        _givableResources = new ArrayList<>();
    }

    public Long getPlayerId() {
        return _playerId;
    }

    public ResourceTrack getResourceTrack() {
        return _resourceTrack;
    }

    public List<ResourceWrap> getResources() {
        return _resources;
    }

    public List<ResCheckResultCode> getResultCodes() {
        return _resultCodes;
    }

    public List<ResourceWrap> getGivableResources() {
        return _givableResources;
    }
}

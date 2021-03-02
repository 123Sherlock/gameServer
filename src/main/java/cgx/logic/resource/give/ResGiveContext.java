package cgx.logic.resource.give;

import cgx.logic.resource.ResourceTrack;
import cgx.logic.resource.ResourceWrap;

/**
 * 给予资源上下文
 */
public class ResGiveContext {

    private final Long _playerId;

    /**
     * 资源内容
     */
    private final ResourceWrap _resource;

    /**
     * 资源来源
     */
    private final ResourceTrack _resourceTrack;

    /**
     * 成功给予的资源
     */
    private ResourceWrap _givenResource;

    public ResGiveContext(Long playerId, ResourceWrap resource, ResourceTrack resourceTrack) {
        _playerId = playerId;
        _resource = resource;
        _resourceTrack = resourceTrack;
    }

    public Long getPlayerId() {
        return _playerId;
    }

    public ResourceWrap getResource() {
        return _resource;
    }

    public ResourceTrack getResourceTrack() {
        return _resourceTrack;
    }

    public ResourceWrap getGivenResource() {
        return _givenResource;
    }

    public void setGivenResource(ResourceWrap givenResource) {
        _givenResource = givenResource;
    }
}

package cgx.logic.resource.give.check.seccheck;

import cgx.logic.player.GamePlayer;
import cgx.logic.resource.ResourceTrack;
import cgx.logic.resource.ResourceWrap;
import cgx.logic.resource.give.check.ResCheckResultCode;

import java.util.List;

public class ResGiveSubCheckContext {

    private final GamePlayer _gamePlayer;

    /**
     * 资源列表
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

    public ResGiveSubCheckContext(GamePlayer gamePlayer, List<ResourceWrap> resources,
        ResourceTrack resourceTrack, List<ResCheckResultCode> resultCodes, List<ResourceWrap> givableResources) {
        _gamePlayer = gamePlayer;
        _resources = resources;
        _resourceTrack = resourceTrack;
        _resultCodes = resultCodes;
        _givableResources = givableResources;
    }

    public GamePlayer getGamePlayer() {
        return _gamePlayer;
    }

    public List<ResourceWrap> getResources() {
        return _resources;
    }

    public ResourceTrack getResourceTrack() {
        return _resourceTrack;
    }

    public List<ResCheckResultCode> getResultCodes() {
        return _resultCodes;
    }

    public List<ResourceWrap> getGivableResources() {
        return _givableResources;
    }
}

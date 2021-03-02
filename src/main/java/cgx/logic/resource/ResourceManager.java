package cgx.logic.resource;

import cgx.logic.define.ErrorCodeDefine;
import cgx.logic.exception.LogicException;
import cgx.logic.player.GamePlayer;
import cgx.logic.resource.give.ResGiveBehavMap;
import cgx.logic.resource.give.ResGiveBehavior;
import cgx.logic.resource.give.ResGiveContext;
import cgx.logic.resource.give.ResGiveResult;
import cgx.logic.resource.give.check.ResCheckResultCode;
import cgx.logic.resource.give.check.ResGiveCheckBehavMap;
import cgx.logic.resource.give.check.ResGiveCheckBehavior;
import cgx.logic.resource.give.check.ResGiveCheckContext;
import cgx.logic.resource.give.check.ResGiveCheckResult;
import cgx.logic.resource.give.check.seccheck.ResGiveSubCheckBehavMap;
import cgx.logic.resource.give.check.seccheck.ResGiveSubCheckBehavior;
import cgx.logic.resource.give.check.seccheck.ResGiveSubCheckContext;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 资源管理器
 */
@Component
public class ResourceManager {

    /**
     * 给与资源的检查
     * @param resourceList 资源列表
     * @param resourceTrack 资源来源
     */
    public ResGiveCheckResult checkGive(GamePlayer gamePlayer,
        List<ResourceWrap> resourceList, ResourceTrack resourceTrack) {
        return checkGiveImpl(gamePlayer, mergeResource(resourceList), resourceTrack);
    }

    private ResGiveCheckResult checkGiveImpl(GamePlayer gamePlayer,
        Map<Integer, Map<Integer, List<ResourceWrap>>> resourceMap, ResourceTrack track) {
        boolean ungivableRes = resourceMap.keySet().stream()
            .anyMatch((t) -> resGiveCheckBehavMap.getBehavior(t) == null); // 找不到对应类型的处理器
        if (ungivableRes) {
            throw new LogicException(ErrorCodeDefine.E3_001);
        }

        List<ResCheckResultCode> resultCodeList = new ArrayList<>(resourceMap.size());
        List<ResourceWrap> givableResList = new ArrayList<>(resourceMap.size());

        for (Map.Entry<Integer, Map<Integer, List<ResourceWrap>>> entry : resourceMap.entrySet()) {
            Map<Integer, List<ResourceWrap>> resourceSubMap = entry.getValue();
            List<ResourceWrap> resources = resourceSubMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

            // 资源大类检查
            ResGiveCheckBehavior checkBehavior = resGiveCheckBehavMap.getBehavior(entry.getKey());
            ResGiveCheckContext checkCtx = new ResGiveCheckContext(gamePlayer.getPlayerId(), resources, track);
            checkBehavior.behave(checkCtx);

            List<ResCheckResultCode> resultCodes = checkCtx.getResultCodes();
            List<ResourceWrap> givableResources = checkCtx.getGivableResources();

            // 有些小类需要特别检查
            subCheck(gamePlayer, resourceSubMap, track, resultCodes, givableResources);
            resultCodeList.addAll(resultCodes);
            givableResList.addAll(givableResources);
        }
        return new ResGiveCheckResult(resultCodeList, givableResList);
    }

    private void subCheck(GamePlayer gamePlayer, Map<Integer, List<ResourceWrap>> resourceSubMap,
        ResourceTrack resourceTrack, List<ResCheckResultCode> resultCodes, List<ResourceWrap> givableResources) {
        for (Map.Entry<Integer, List<ResourceWrap>> entry : resourceSubMap.entrySet()) {
            ResGiveSubCheckBehavior checkBehavior = resGiveSubCheckBehavMap.getBehavior(entry.getKey());
            if (checkBehavior == null) {
                // 小类检查非必须
                continue;
            }
            ResGiveSubCheckContext checkCtx = new ResGiveSubCheckContext(gamePlayer,
                entry.getValue(), resourceTrack, resultCodes, givableResources);
            checkBehavior.behave(checkCtx);
        }
    }

    /**
     * 给与能获取部分的资源
     * TODO: 根据需求，背包不足时可能发邮件，也可能发能给的部分
     * TODO: 应用buff系统
     */
    private ResGiveResult give(GamePlayer gamePlayer, List<ResourceWrap> resourceList, ResourceTrack track) {
        boolean ungivableRes = resourceList.stream()
            .map(ResourceWrap::getType)
            .anyMatch((t) -> resGiveBehavMap.getBehavior(t) == null); // 找不到对应类型的处理器
        if (ungivableRes) {
            throw new LogicException(ErrorCodeDefine.E3_001);
        }

        List<ResourceWrap> givenResList = new ArrayList<>(resourceList.size());
        for (ResourceWrap resource : resourceList) {
            ResGiveContext resGiveCtx = new ResGiveContext(gamePlayer.getPlayerId(), resource, track);
            ResGiveBehavior behavior = resGiveBehavMap.getBehavior(resource.getType());
            behavior.behave(resGiveCtx);
            givenResList.add(resGiveCtx.getGivenResource());
        }
        return new ResGiveResult(ImmutableList.of(), givenResList);
    }

    /**
     * 检查并给予资源，当所有资源都能完整获取时才会给
     * @param resourceList 资源列表
     * @param track 资源来源
     * @return 检查结果
     */
    public ResGiveResult checkAndGive(GamePlayer gamePlayer, List<ResourceWrap> resourceList, ResourceTrack track) {
        ResGiveCheckResult checkResult = checkGiveImpl(gamePlayer, mergeResource(resourceList), track);
        boolean checkFail = checkResult.getResultCodeList().stream()
            .map(ResCheckResultCode::getErrorCode)
            .anyMatch((c) -> c != ErrorCodeDefine.SUCCESS);
        if (checkFail) {
            return new ResGiveResult(checkResult.getResultCodeList(), null);
        }
        return give(gamePlayer, checkResult.getGivableResList(), track);
    }

    /**
     * 合并整理资源
     */
    private Map<Integer, Map<Integer, List<ResourceWrap>>> mergeResource(List<ResourceWrap> resourceList) {
        // Map<大类, Map<小类, List<资源>>>
        Map<Integer, Map<Integer, List<ResourceWrap>>> typeMap = new HashMap<>(resourceList.size());

        for (ResourceWrap resource : resourceList) {
            Integer type = resource.getType();
            Integer subType = resource.getSubType();
            Integer configId = resource.getConfigId();
            long duration = resource.getDuration();

            Map<Integer, List<ResourceWrap>> subTypeMap = typeMap.get(type);
            if (subTypeMap == null) {
                List<ResourceWrap> newResourceList = new ArrayList<>();
                newResourceList.add(resource);

                Map<Integer, List<ResourceWrap>> newSubTypeMap = new HashMap<>();
                newSubTypeMap.put(subType, newResourceList);
                typeMap.put(type, newSubTypeMap);
            } else {
                List<ResourceWrap> resourceWraps = subTypeMap.computeIfAbsent(subType, ArrayList::new);
                Optional<ResourceWrap> resourceOpt = resourceWraps.stream()
                    .filter((r) -> r.getConfigId().equals(configId))
                    .filter((r) -> r.getDuration() == duration)
                    .findAny();

                if (resourceOpt.isPresent()) {
                    ResourceWrap resourceWrap = resourceOpt.get();
                    resourceWrap.setAmount(resourceWrap.getAmount() + resource.getAmount());
                } else {
                    resourceWraps.add(resource);
                }
            }
        }
        return typeMap;
    }

    @Autowired
    private ResGiveCheckBehavMap resGiveCheckBehavMap;

    @Autowired
    private ResGiveSubCheckBehavMap resGiveSubCheckBehavMap;

    @Autowired
    private ResGiveBehavMap resGiveBehavMap;
}

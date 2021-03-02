package cgx.logic.player;

import cgx.logic.define.ErrorCodeDefine;
import cgx.logic.exception.LogicException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerService {

    private final Map<Long, GamePlayer> onlinePlayerMap = new ConcurrentHashMap<>();

    public GamePlayer getOnlinePlayer(Long id) {
        return onlinePlayerMap.get(id);
    }

    public GamePlayer addOnlinePlayer(GamePlayer gamePlayer) {
        Long playerId = gamePlayer.getPlayerId();
        if (playerId == null || playerId <= 0) {
            throw new LogicException(ErrorCodeDefine.E1_001);
        }
        return onlinePlayerMap.put(playerId, gamePlayer);
    }

    public void removeOnlinePlayer(Long playerId) {
        onlinePlayerMap.remove(playerId);
    }

    public boolean isOnline(Long playerId) {
        return onlinePlayerMap.containsKey(playerId);
    }
}

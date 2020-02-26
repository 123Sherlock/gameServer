package cgx.logic.player;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

public class PlayerService {

    private static final Set<Long> onlinePlayerIdSet = Sets.newConcurrentHashSet();

    public static Set<Long> getOnlinePlayerIdSet() {
        return ImmutableSet.copyOf(onlinePlayerIdSet);
    }

    public static void addOnlinePlayerId(Long playerId) {
        onlinePlayerIdSet.add(playerId);
    }

    public static void removeOnlinePlayerId(Long playerId) {
        onlinePlayerIdSet.remove(playerId);
    }
}

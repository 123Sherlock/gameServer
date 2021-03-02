package cgx.logic.login.manager;

import cgx.logic.define.ErrorCodeDefine;
import cgx.logic.exception.LogicException;
import cgx.logic.player.GamePlayer;
import cgx.logic.player.PlayerDb;
import cgx.logic.player.PlayerDbGetter;
import cgx.logic.player.PlayerService;
import cgx.proto.ProtoLogin;
import cgx.proto.ProtoPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginManager {

    public ProtoLogin.Login.S2C login(GamePlayer gamePlayer, Long playerId) {
        if (playerService.isOnline(playerId)) {
            throw new LogicException(ErrorCodeDefine.E2_001);
        }

        PlayerDb playerDb = playerDbGetter.get(playerId);
        gamePlayer.setPlayerId(playerDb.getId());

        ProtoPlayer.Player playerP = ProtoPlayer.Player.newBuilder()
            .setId(playerId)
            .setName(playerDb.getName())
            .setLevel(playerDb.getLevel())
            .build();

        GamePlayer oldPlayer = playerService.addOnlinePlayer(gamePlayer);
        if (oldPlayer != null) {
            throw new LogicException(ErrorCodeDefine.E2_001);
        }

        return ProtoLogin.Login.S2C.newBuilder()
            .setPlayer(playerP)
            .build();
    }

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerDbGetter playerDbGetter;
}

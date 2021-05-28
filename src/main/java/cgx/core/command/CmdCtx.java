package cgx.core.command;

import cgx.core.netty.Message;
import cgx.logic.player.GamePlayer;

public class CmdCtx {

    private final GamePlayer _gamePlayer;

    private final Message _msg;

    public CmdCtx(GamePlayer gamePlayer, Message msg) {
        _gamePlayer = gamePlayer;
        _msg = msg;
    }

    public GamePlayer getGamePlayer() {
        return _gamePlayer;
    }

    public Message getMsg() {
        return _msg;
    }
}

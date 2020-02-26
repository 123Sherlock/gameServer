package cgx.core.command;

import cgx.core.netty.msg.RequestMsg;
import cgx.logic.player.GamePlayer;

public interface Command {

    void execute(GamePlayer gamePlayer, RequestMsg requestMsg) throws Exception;
}

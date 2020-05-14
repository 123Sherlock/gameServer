package cgx.core.command;

import cgx.core.netty.Message;
import cgx.logic.player.GamePlayer;

public interface Command {

    void execute(GamePlayer gamePlayer, Message requestMsg) throws Exception;
}

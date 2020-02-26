package cgx.logic.login.cmd;

import cgx.core.command.Cmd;
import cgx.core.command.Command;
import cgx.core.netty.msg.RequestMsg;
import cgx.logic.define.CommandDefine;
import cgx.logic.player.GamePlayer;

@Cmd(cmdId = CommandDefine.C1_001)
public class LoginCmd implements Command {

    @Override
    public void execute(GamePlayer gamePlayer, RequestMsg requestMsg) throws Exception {

    }
}

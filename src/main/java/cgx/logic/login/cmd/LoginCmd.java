package cgx.logic.login.cmd;

import cgx.core.command.Cmd;
import cgx.core.command.Command;
import cgx.core.netty.Message;
import cgx.logic.define.CommandDefine;
import cgx.logic.login.manager.LoginManager;
import cgx.logic.player.GamePlayer;
import cgx.proto.ProtoLogin;
import org.springframework.beans.factory.annotation.Autowired;

@Cmd(cmdId = CommandDefine.C1_001)
public class LoginCmd implements Command {

    @Override
    public void execute(GamePlayer gamePlayer, Message requestMsg) throws Exception {
        ProtoLogin.Login.C2S c2S = requestMsg.getValue(ProtoLogin.Login.C2S.class);
        ProtoLogin.Login.S2C s2C = loginManager.login(gamePlayer, c2S.getPlayerId());
        gamePlayer.sendToClient(s2C);
    }

    @Autowired
    private LoginManager loginManager;
}

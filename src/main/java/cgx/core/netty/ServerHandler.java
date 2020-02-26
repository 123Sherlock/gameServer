package cgx.core.netty;

import cgx.core.command.Command;
import cgx.logic.define.CommandDefine;
import cgx.core.command.CommandSet;
import cgx.core.netty.msg.RequestMsg;
import cgx.logic.player.GamePlayer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final AttributeKey<GamePlayer> gamePlayerKey = AttributeKey.valueOf("gamePlayer");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RequestMsg requestMsg = (RequestMsg) msg;
        System.err.println(String.format("client msg received:%s", requestMsg));

        int cmdId = requestMsg.getCmd();
        Command command = CommandSet.getCommand(cmdId);
        if (command == null) {
            System.err.println(String.format("unknow cmdId:%s", cmdId));
            return;
        }

        if (cmdId == CommandDefine.C1_001) {

        }
        Attribute<GamePlayer> gamePlayerAttr = ctx.channel().attr(gamePlayerKey);
        GamePlayer gamePlayer = gamePlayerAttr.get();
        try {
            command.execute(gamePlayer, requestMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ctx.writeAndFlush(requestMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.err.println(String.format("client connected:%s", ctx.channel()));
    }
}

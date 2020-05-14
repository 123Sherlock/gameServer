package cgx.core.netty;

import cgx.core.command.Command;
import cgx.core.utils.CommonMsgWrapper;
import cgx.logic.define.CommandDefine;
import cgx.core.command.CommandSet;
import cgx.logic.exception.LogicException;
import cgx.logic.player.GamePlayer;
import cgx.proto.ProtoCommonMsg;
import cgx.proto.ProtoError;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final AttributeKey<GamePlayer> GAME_PLAYER_KEY = AttributeKey.valueOf("gamePlayer");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        GamePlayer gamePlayer = ctx.channel().attr(GAME_PLAYER_KEY).get();
        if (gamePlayer == null) {
            return;
        }

        Message requestMsg = (Message) msg;
        System.err.println(String.format("Client msg received[%s]", requestMsg));

        int cmdId = requestMsg.getCmd();
        Command command = CommandSet.getCommand(cmdId);
        if (command == null) {
            System.err.println(String.format("Unknown cmdId[%s]", cmdId));
            return;
        }

        if (cmdId != CommandDefine.C1_001) {
            ProtoCommonMsg.Msg msgValue = requestMsg.getValue(ProtoCommonMsg.Msg.class);
            long playerId = msgValue.getPlayerId();
            if (playerId <= 0) {
                return;
            }
            gamePlayer.setPlayerId(playerId);
        }
        try {
            command.execute(gamePlayer, requestMsg);
        } catch (LogicException e) {
            ProtoError.Error errorMsg = ProtoError.Error.newBuilder()
                .setCode(e.getCode())
                .build();

            ProtoCommonMsg.Msg errorResponse = CommonMsgWrapper.wrap(cmdId, errorMsg);
            ctx.writeAndFlush(errorResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setChannel(ctx.channel());
        ctx.channel().attr(GAME_PLAYER_KEY).set(gamePlayer);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.err.println(String.format("Client connected[%s]", ctx.channel()));
    }
}

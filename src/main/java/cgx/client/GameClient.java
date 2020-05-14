package cgx.client;

import cgx.core.netty.ProtobufToMessageCodec;
import cgx.core.utils.CommonMsgWrapper;
import cgx.logic.define.CommandDefine;
import cgx.proto.ProtoCommonMsg;
import cgx.proto.ProtoLogin;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class GameClient {

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 8080;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                .addLast(new ProtobufVarint32FrameDecoder())
                                .addLast(new ProtobufDecoder(ProtoCommonMsg.Msg.getDefaultInstance()))
                                .addLast(new ProtobufVarint32LengthFieldPrepender())
                                .addLast(new ProtobufEncoder())
                                .addLast(new ProtobufToMessageCodec())
                                .addLast(new ClientHandler());
                        }
                    });

            ChannelFuture cf = bootstrap.connect(host, port).sync();
            ProtoLogin.Login.C2S loginC2s = ProtoLogin.Login.C2S.newBuilder()
                .setPlayerId(1001)
                .build();

            cf.channel().writeAndFlush(CommonMsgWrapper.wrap(CommandDefine.C1_001, loginC2s));
            cf.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

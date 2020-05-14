package cgx.core;

import cgx.core.command.CommandSet;
import cgx.core.database.MysqlService;
import cgx.core.inject.ServerInjectConfig;
import cgx.core.netty.ProtobufToMessageCodec;
import cgx.core.netty.ServerHandler;
import cgx.core.spring.BeanCollector;
import cgx.core.utils.PropertyUtils;
import cgx.proto.ProtoCommonMsg;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class GameServer {

    private static GameServer gameServer;

    public static GameServer getInstance() {
        return gameServer;
    }

    @Autowired
    private BeanCollector beanCollector;

    public static void main(String[] args) {
        try (ConfigurableApplicationContext appCtx = new AnnotationConfigApplicationContext(ServerInjectConfig.class)) {
            gameServer = appCtx.getBean(GameServer.class);
            gameServer.initCmdSet();

            Properties properties = PropertyUtils.getProperties();
            initMysql(properties);
            initNetty(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initNetty(Properties properties) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // 用来接收进来的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // 用来处理已经被接收的连接

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                .addLast(new ProtobufVarint32FrameDecoder())
                                .addLast(new ProtobufDecoder(ProtoCommonMsg.Msg.getDefaultInstance()))
                                .addLast(new ProtobufVarint32LengthFieldPrepender())
                                .addLast(new ProtobufEncoder())
                                .addLast(new ProtobufToMessageCodec())
                                .addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);

            // 绑定端口，开始接收进来的连接
            int port = Integer.parseInt(properties.getProperty("port"));
            ChannelFuture f = serverBootstrap.bind(port).sync();

            // 等待服务器socket关闭
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private static void initMysql(Properties properties) {
        MysqlService.initMysqlServer(properties);
    }

    private void initCmdSet() {
        CommandSet.load(beanCollector);
    }
}

package cgx.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class GameClient {

    /**
     * Netty 客户端的通信步骤大致为：
     * 创建一个 NIO 线程组，用于处理服务器与客户端的连接，客户端不需要用到 boss worker。
     * 创建一个 Bootstrap 对象，配置 Netty 的一系列参数，由于客户端 SocketChannel 没有父亲，所以不需要使用 childOption。
     * 创建一个用于实际处理数据的类ChannelInitializer，进行初始化的准备工作，比如设置接受传出数据的字符集、格式以及实际处理数据的接口。
     * 配置服务器 IP 和端口号，建立与服务器的连接。
     */
    public static void main(String[] args) throws Exception {
//        Bootstrap bootstrap = new Bootstrap();
//        NioEventLoopGroup group = new NioEventLoopGroup();
//
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .handler(new ChannelInitializer<Channel>() {
//                    @Override
//                    protected void initChannel(Channel ch) {
//                        ch.pipeline().addLast(new StringEncoder());
//                    }
//                });
//
//        Channel channel = bootstrap.connect("127.0.0.1", 8000).channel();
//
//        while (true) {
//            channel.writeAndFlush(new Date() + ": hello world!");
//            Thread.sleep(2000);
//        }

        String host = "127.0.0.1";            // ip
        int port = 8080;                    // 端口
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()            // 与ServerBootstrap类似
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)    // 客户端的socketChannel没有父亲
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            // 启动客户端，客户端用connect连接
            ChannelFuture f = bootstrap.connect(host, port).sync();

            // 等待连接关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

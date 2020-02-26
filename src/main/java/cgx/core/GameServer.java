package cgx.core;

import cgx.core.command.CommandSet;
import cgx.core.database.MysqlService;
import cgx.core.inject.ServerInjectConfig;
import cgx.core.netty.ServerHandler;
import cgx.core.spring.BeanCollector;
import cgx.core.utils.PropertyUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
        ConfigurableApplicationContext appCtx = new AnnotationConfigApplicationContext(ServerInjectConfig.class);
        try {
            Properties properties = PropertyUtils.getProperties();
            initNetty(properties);
            initMysql(properties);

            gameServer = appCtx.getBean(GameServer.class);
            gameServer.initCmdSet();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            appCtx.close();
        }
    }

    /**
     * public static void main(String[] args) {
     * NioEventLoopGroup boosGroup = new NioEventLoopGroup();
     * NioEventLoopGroup workerGroup = new NioEventLoopGroup();
     * <p>
     * ServerBootstrap serverBootstrap = new ServerBootstrap();
     * serverBootstrap
     * .group(boosGroup, workerGroup)
     * .channel(NioServerSocketChannel.class)
     * .childHandler(new ChannelInitializer<NioSocketChannel>() {
     * protected void initChannel(NioSocketChannel ch) {
     * }
     * });
     * <p>
     * serverBootstrap.bind(8000);
     * }
     * <p>
     * 首先看到，我们创建了两个NioEventLoopGroup，这两个对象可以看做是传统IO编程模型的两大线程组，
     * boosGroup表示监听端口，创建新连接的线程组，workerGroup表示处理每一条连接的数据读写的线程组，
     * 用生活中的例子来讲就是，一个工厂要运作，必然要有一个老板负责从外面接活，然后有很多员工，负责具体干活，
     * 老板就是boosGroup，员工们就是workerGroup，boosGroup接收完连接，扔给workerGroup去处理。
     * 接下来 我们创建了一个引导类 ServerBootstrap，这个类将引导我们进行服务端的启动工作，直接new出来开搞。
     * 我们通过.group(boosGroup, workerGroup)给引导类配置两大线程，这个引导类的线程模型也就定型了。
     * 然后，我们指定我们服务端的IO模型为NIO，我们通过.channel(NioServerSocketChannel.class)来指定IO模型，
     * 当然，这里也有其他的选择，如果你想指定IO模型为BIO，那么这里配置上OioServerSocketChannel.class类型即可，
     * 当然通常我们也不会这么做，因为Netty的优势就在于NIO。
     * 接着，我们调用childHandler()方法，给这个引导类创建一个ChannelInitializer，
     * 这里主要就是定义后续每条连接的数据读写，业务处理逻辑，不理解没关系，在后面我们会详细分析。
     * ChannelInitializer这个类中，我们注意到有一个泛型参数NioSocketChannel，
     * 这个类呢，就是Netty对NIO类型的连接的抽象，而我们前面NioServerSocketChannel也是对NIO类型的连接的抽象，
     * NioServerSocketChannel和NioSocketChannel的概念可以和BIO编程模型中的ServerSocket以及Socket两个概念对应上
     * 我们的最小化参数配置到这里就完成了，我们总结一下就是，要启动一个Netty服务端，必须要指定三类属性，
     * 分别是线程模型、IO模型、连接读写处理逻辑，有了这三者，之后在调用bind(8000)，我们就可以在本地绑定一个8000端口启动起来
     */
    private static void initNetty(Properties properties) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();        // 用来接收进来的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();    // 用来处理已经被接收的连接

        try {

//            ServerBootstrap serverBootstrap = new ServerBootstrap();
//            NioEventLoopGroup boos = new NioEventLoopGroup();
//            NioEventLoopGroup worker = new NioEventLoopGroup();
//            serverBootstrap
//                    .group(boos, worker)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
//                        protected void initChannel(NioSocketChannel c) {
//                            c.pipeline().addLast(new StringDecoder());
//                            c.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
//                                @Override
//                                protected void channelRead0(ChannelHandlerContext ctx, String i) {
//                                    System.out.println(i);
//                                }
//                            });
//                        }
//                    })
//                    .bind(8000);

            ServerBootstrap serverBootstrap = new ServerBootstrap()        // 启动NIO服务的辅助启动类
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)            // 这里告诉Channel如何接收新的连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 自定义处理类
                            // 注意添加顺序
                            ch.pipeline().addLast(new ServerHandler());
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

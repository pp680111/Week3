package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.child.ChildChannelInitializer;

public class GatewayServer {
    private static final Logger LOG = LoggerFactory.getLogger(GatewayServer.class);
    private static final int DEFAULT_PORT = 8808;

    private int port;

    public GatewayServer() {
        this.port = DEFAULT_PORT;
    }

    public GatewayServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup, childEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ChildChannelInitializer());
            ChannelFuture future = serverBootstrap.bind(this.port).sync();
            // 等待ServerChannel关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("Server Channel is interrupted", e);
        }
    }
}

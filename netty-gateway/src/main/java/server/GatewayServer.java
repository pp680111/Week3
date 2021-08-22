package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.child.ProxyChildChannelInitializer;
import server.filter.AddSpecifiedHeaderRequestFilter;
import server.filter.HttpRequestFilterHandler;
import server.filter.HttpResponseFilterHandler;
import server.filter.ProxyEndTimeResponseFilter;
import server.filter.ProxyStartTimeRequestFilter;

/**
 * 网关Server端
 * @author zst
 */
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
//                    .handler(new LoggingHandler())
                    .childHandler(new ProxyChildChannelInitializer());
            ChannelFuture future = serverBootstrap.bind(this.port).sync();
            // 等待ServerChannel关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("Server Channel is interrupted", e);
        }
    }
}

package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.child.ProxyChildChannelInitializer;

/**
 * 网关Server端
 * @author zst
 */
public class GatewayServer {
    private static final Logger LOG = LoggerFactory.getLogger(GatewayServer.class);
    private static final int DEFAULT_PORT = 8808;

    private int port;
    private String proxyPath;

    public GatewayServer(String proxyPath) {
        this.port = DEFAULT_PORT;
        this.proxyPath = proxyPath;
    }

    public GatewayServer(int port, String proxyPath) {
        this.port = port;
        this.proxyPath = proxyPath;
    }

    public void start() {
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
        EventLoopGroup childEventLoopGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup, childEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler())
                    .childHandler(new ProxyChildChannelInitializer(proxyPath));
            ChannelFuture future = serverBootstrap.bind(this.port).sync();
            // 等待ServerChannel关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("Server Channel is interrupted", e);
        }
    }
}

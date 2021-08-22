package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyHttpRequestClient {
    private static final Logger LOG = LoggerFactory.getLogger(NettyHttpRequestClient.class);
    private Channel channel;
    private String host;
    private int port;

    public NettyHttpRequestClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(new NioEventLoopGroup(1))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .channel(NioSocketChannel.class)
                    .handler(new RequestClientInitializer());
            ChannelFuture future = bootstrap.bind(this.host, this.port).sync();
            this.channel = future.channel();
            ChannelFuture closeFuture = future.channel().closeFuture();
            closeFuture.addListener(f -> {
                this.channel = null;
            });
            closeFuture.sync();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void send(FullHttpRequest request) {
        if (channel == null) {
            throw new IllegalStateException("No connection");
        }

    }
}

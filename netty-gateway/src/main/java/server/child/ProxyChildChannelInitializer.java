package server.child;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class ProxyChildChannelInitializer extends ChannelInitializer<SocketChannel> {
    private String proxyPath;

    public ProxyChildChannelInitializer(String proxyPath) {
        this.proxyPath = proxyPath;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(new HttpProxyHandler(this.proxyPath));
//                .addLast(new AsyncHttpProxyHandler(this.proxyPath));
    }
}

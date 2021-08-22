package server.child;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import server.filter.AddSpecifiedHeaderRequestFilter;
import server.filter.HttpRequestFilterHandler;
import server.filter.HttpResponseFilter;
import server.filter.HttpResponseFilterHandler;
import server.filter.ProxyEndTimeResponseFilter;
import server.filter.ProxyStartTimeRequestFilter;

public class ProxyChildChannelInitializer extends ChannelInitializer<SocketChannel> {
    private String proxyPath;

    public ProxyChildChannelInitializer(String proxyPath) {
        this.proxyPath = proxyPath;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(createHttpRequestFilterHandler())
                .addLast(createHttpResponseFilterHandler())
                .addLast(new HttpProxyHandler(this.proxyPath));
//                .addLast(new AsyncHttpProxyHandler(this.proxyPath));
    }


    /*
       下面两段创建拦截器Handler的代码，可以想想怎么更加优雅的创建，是所有的Channel通用一段拦截器链，还是每个Channel各自创建新的，
       共享拦截器链的话有什么风险
     */

    private HttpResponseFilterHandler createHttpResponseFilterHandler() {
        HttpResponseFilterHandler responseFilterHandler = new HttpResponseFilterHandler();
        responseFilterHandler.addLast(new ProxyEndTimeResponseFilter());
        return responseFilterHandler;
    }

    private HttpRequestFilterHandler createHttpRequestFilterHandler() {
        HttpRequestFilterHandler requestFilterHandler = new HttpRequestFilterHandler();
        requestFilterHandler.addLast(new ProxyStartTimeRequestFilter());
        requestFilterHandler.addLast(new AddSpecifiedHeaderRequestFilter("Sky", "IsBlue"));
        return requestFilterHandler;
    }
}

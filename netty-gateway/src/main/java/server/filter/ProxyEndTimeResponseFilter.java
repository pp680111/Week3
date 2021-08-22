package server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 添加代理结束时间的拦截器
 */
public class ProxyEndTimeResponseFilter implements HttpResponseFilter {
    @Override
    public boolean doResponseFilter(FullHttpResponse response, ChannelHandlerContext ctx) {
        if (response != null) {
            response.headers().set("Proxy-End-Time", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        }
        return true;
    }
}

package server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 添加时间戳的拦截器
 */
public class ProxyStartTimeRequestFilter implements HttpRequestFilter {
    @Override
    public boolean doRequestFilter(FullHttpRequest request, ChannelHandlerContext ctx) {
        if (request != null) {
            request.headers().add("Proxy-Start-Time", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        }
        return true;
    }
}

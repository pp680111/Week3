package server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 一个简单的添加固定HTTP Header值的过滤器
 */
public class AddSpecifiedHeaderRequestFilter implements HttpRequestFilter {
    private String key;
    private String value;

    public AddSpecifiedHeaderRequestFilter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean doRequestFilter(FullHttpRequest request, ChannelHandlerContext ctx) {
        if (request != null) {
            request.headers().add(key, value);
        }
        return true;
    }
}

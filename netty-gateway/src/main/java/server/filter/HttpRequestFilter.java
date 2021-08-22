package server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Http请求过滤器接口
 * @author zst
 */
public interface HttpRequestFilter {
    /**
     * HTTP Request拦截类
     * @param request
     * @param ctx
     * @return 继续下一个拦截器的执行时返回true，中断后续的拦截器和ChannelHandler的执行的话返回false
     */
    boolean doRequestFilter(FullHttpRequest request, ChannelHandlerContext ctx);
}

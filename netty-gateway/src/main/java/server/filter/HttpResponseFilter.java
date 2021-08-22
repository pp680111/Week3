package server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

public interface HttpResponseFilter {
    /**
     * HTTP Response拦截器类
     * @param response
     * @param ctx
     * @return 继续下一个拦截器的执行时返回true，中断后续的拦截器和ChannelHandler的执行的话返回false
     */
    boolean doResponseFilter(FullHttpResponse response, ChannelHandlerContext ctx);
}

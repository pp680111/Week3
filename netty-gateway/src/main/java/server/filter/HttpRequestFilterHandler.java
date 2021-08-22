package server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class HttpRequestFilterHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestFilterHandler.class);

    private LinkedList<HttpRequestFilter> filterChain;

    public HttpRequestFilterHandler() {
        filterChain = new LinkedList<>();
    }

    public void addLast(HttpRequestFilter filter) {
        if (filter != null) {
            this.filterChain.addLast(filter);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpRequest request = (FullHttpRequest) msg;
            for (HttpRequestFilter filter : this.filterChain) {
                if (!filter.doRequestFilter(request, ctx)) {
                    return;
                }
            }
            ctx.fireChannelRead(msg);
        } catch (Exception e) {
            LOG.error("HTTP request filter execute error", e);
        }
    }
}

package server.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class HttpResponseFilterHandler extends ChannelOutboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestFilterHandler.class);

    private LinkedList<HttpResponseFilter> filterChain;

    public HttpResponseFilterHandler() {
        filterChain = new LinkedList<>();
    }

    public void addLast(HttpResponseFilter filter) {
        if (filter != null) {
            this.filterChain.addLast(filter);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        try {
            FullHttpResponse response = (FullHttpResponse) msg;
            for (HttpResponseFilter filter : this.filterChain) {
                if (!filter.doResponseFilter(response, ctx)) {
                    return;
                }
            }
            ctx.write(response, promise);
        } catch (Exception e) {
            LOG.error("HTTP response filter execute error", e);
        }
    }
}

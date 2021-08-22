package server.child;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.ReferenceCountUtil;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.router.Router;
import server.router.exception.NoBackendException;

import java.io.IOException;

/**
 * 同步Http请求转发Handler
 * @author zst
 */
public class HttpProxyHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(HttpProxyHandler.class);
    private Router router;

    public HttpProxyHandler(Router router) {
        this.router = router;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest request = (FullHttpRequest) msg;
            requestHandler(ctx, request);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void requestHandler(ChannelHandlerContext ctx, FullHttpRequest request) {
        FullHttpResponse response = null;
        try {
            String proxyUri = getProxyUri(request);
            LOG.debug(String.format("proxyUri = %s", proxyUri));

            Request proxyRequest = Request.Get(proxyUri);
            request.headers().forEach(header -> {
                proxyRequest.setHeader(header.getKey(), header.getValue());
            });

            HttpResponse proxyResponse = proxyRequest.execute().returnResponse();
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(EntityUtils.toString(proxyResponse.getEntity()).getBytes()));
            for (Header h : proxyResponse.getAllHeaders()) {
                response.headers().set(h.getName(), h.getValue());
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        } catch (NoBackendException ne) {
            LOG.error(ne.getMessage(), ne);
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
        } finally {
            if (!HttpUtil.isKeepAlive(request)) {
                ctx.write(response).addListener(ChannelFutureListener.CLOSE);
            } else {
                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                ctx.write(response);
            }
        }
    }

    private String getProxyUri(FullHttpRequest request) throws NoBackendException {
        return this.router.route(request);
    }
}

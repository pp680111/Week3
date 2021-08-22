package server.router;

import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;
import server.router.exception.NoBackendException;
import server.router.matcher.ServerUpstreamMatcher;
import server.router.rule.Rule;

import java.util.List;

public class Router {
    private ServerUpstreamMatcher matcher;
    private Rule rule;

    public Router(ServerUpstreamMatcher matcher, Rule rule) {
        this.matcher = matcher;
        this.rule = rule;
    }

    public String route(FullHttpRequest request) throws NoBackendException {
        List<String> upstreams = this.matcher.upstreams(request.uri());
        String backendPath = this.rule.getTargetPath(upstreams, request);
        if (StringUtils.isEmpty(backendPath)) {
            throw new NoBackendException();
        }

        return backendPath + this.matcher.afterMatchPath(request.uri());
    }
}

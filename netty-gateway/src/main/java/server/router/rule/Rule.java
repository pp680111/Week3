package server.router.rule;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

public interface Rule {
    String getTargetPath(List<String> upstream, FullHttpRequest request);
}

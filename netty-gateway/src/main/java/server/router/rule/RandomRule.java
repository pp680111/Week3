package server.router.rule;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

public class RandomRule implements Rule {
    @Override
    public String getTargetPath(List<String> upstream, FullHttpRequest request) {
        return upstream.get((int) (Math.random() * upstream.size()));
    }
}

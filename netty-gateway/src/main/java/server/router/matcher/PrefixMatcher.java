package server.router.matcher;

import config.ServerCfgContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.Collections;
import java.util.List;

public class PrefixMatcher implements ServerUpstreamMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(PrefixMatcher.class);
    @Override
    public List<String> upstreams(String uri) {
        try {
            // 因为url拿到的path最前面有一个/，去掉
            if (uri.length() > 0) {
                uri = uri.substring(1);
            }
            int slashIndex = uri.indexOf('/');
            if (slashIndex == -1) {
                return ServerCfgContext.getUpstream(uri);
            } else {
                return ServerCfgContext.getUpstream(uri.substring(0, slashIndex));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return Collections.emptyList();
    }

    @Override
    public String afterMatchPath(String uri) {
        if (StringUtils.isEmpty(uri)) {
            return uri;
        }
        try {
            if (uri.length() > 0) {
                uri = uri.substring(1);
            }
            int slashIndex = uri.indexOf('/');
            if (slashIndex != -1) {
                return uri.substring(slashIndex);
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return "";
    }

    private String getPrefix(String path) {
        int slashIndex = path.indexOf('/');
        if (slashIndex == -1) {
            return path;
        } else {
            return path.substring(0, slashIndex);
        }
    }
}

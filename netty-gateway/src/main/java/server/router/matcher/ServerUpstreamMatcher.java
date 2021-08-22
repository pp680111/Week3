package server.router.matcher;

import java.util.List;

public interface ServerUpstreamMatcher {
    /**
     * 根据URL找到匹配的后端地址列表
     * @param uri 请求地址
     * @return
     */
    List<String> upstreams(String uri);

    /**
     * 计算访问后端服务时的path
     * 由于url中包含了一些匹配后端用的信息，所以要处理掉，变成真正能访问到后端api的path
     * @param uri
     * @return
     */
    String afterMatchPath(String uri);
}

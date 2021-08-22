package config;

import java.util.List;

/**
 * 后端配置信息实体类
 */
public class ServerCfg {
    /** 匹配模式*/
    private String pattern;
    /** 转发地址列表*/
    private List<String> upstream;

    public ServerCfg(String pattern, List<String> upstream) {
        this.pattern = pattern;
        this.upstream = upstream;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<String> getUpstream() {
        return upstream;
    }

    public void setUpstream(List<String> upstream) {
        this.upstream = upstream;
    }
}

package config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServerCfgContext {
    private static final Map<String, ServerCfg> serverCfgMap;
    static {
        InputStream cfgFileStream = ServerCfgContext.class.getClassLoader()
                .getResourceAsStream("server.yml");
        Yaml yaml = new Yaml();
        List<Map<String, Object>> serverList = yaml.load(new InputStreamReader(cfgFileStream));
        serverCfgMap = new HashMap<>(serverList.size());
        serverList.forEach(server -> {
            ServerCfg cfg = new ServerCfg(server.get("pattern").toString(),
                    (List<String>) server.get("upstream"));
            serverCfgMap.put(cfg.getPattern(), cfg);
        });
    }

    public static List<String> getUpstream(String pattern) {
        if (serverCfgMap.containsKey(pattern)) {
            return serverCfgMap.get(pattern).getUpstream();
        }
        return Collections.emptyList();
    }

    public static Set<String> getPatterns() {
        return serverCfgMap.keySet();
    }
}

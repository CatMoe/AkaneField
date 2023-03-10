package catmoe.fallencrystal.akanefield.common.objects.config;

import catmoe.fallencrystal.akanefield.common.IConfiguration;

public class ProxyCheckConfig {
    private final String key;
    private final boolean checkFastJoin;

    public ProxyCheckConfig(IConfiguration config) {
        this.key = config.getString("proxycheck.api-key");
        this.checkFastJoin = true;
    }

    public String getKey() {
        return key;
    }

    public boolean isCheckFastJoin() {
        return checkFastJoin;
    }

    public boolean isEnabled() {
        return key.length() == 27;
    }
}

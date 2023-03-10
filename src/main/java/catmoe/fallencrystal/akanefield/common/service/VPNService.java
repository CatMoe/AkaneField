package catmoe.fallencrystal.akanefield.common.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.IService;
import catmoe.fallencrystal.akanefield.common.antivpn.ipapi.IPAPIProvider;
import catmoe.fallencrystal.akanefield.common.antivpn.proxycheck.ProxyCheckProvider;
import catmoe.fallencrystal.akanefield.common.helper.LogHelper;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;

public class VPNService implements IService {

    private final IAntiBotPlugin plugin;
    private final LogHelper logHelper;
    private final List<String> underVerification;

    private int currentChecks;

    public VPNService(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        logHelper = plugin.getLogHelper();
        this.underVerification = new ArrayList<>();
        this.currentChecks = 0;
        plugin.scheduleRepeatingTask(() -> {
            currentChecks = 0;
            underVerification.clear();
        }, true, TimeUnit.MINUTES.toMillis(1));
    }

    @Override
    public void load() {
        logHelper.debug("Loaded " + this.getClass().getSimpleName() + "!");
    }

    @Override
    public void unload() {

    }

    public void submitIP(String ip, String name) {
        if (underVerification.contains(ip)) {
            return;
        }
        IAntiBotManager antiBotManager = plugin.getAntiBotManager();

        underVerification.add(ip);
        plugin.scheduleDelayedTask(() -> {
            if (underVerification.size() > 4) {
                logHelper.debug("Too many verification requests! - Clearing...");
                underVerification.clear();
                return;
            }
            if (antiBotManager.getBlackListService().isBlackListed(ip)) {
                return;
            }
            if (ConfigManager.isIPApiVerificationEnabled && currentChecks < 45) {
                new IPAPIProvider(plugin).process(ip, name);
                underVerification.remove(ip);
                currentChecks++;
            }

            if (!ConfigManager.getProxyCheckConfig().isEnabled()) {
                underVerification.remove(ip);
                logHelper.debug("API key not set! - ProxyCheck is offline!");
                return;
            }
            underVerification.remove(ip);
            new ProxyCheckProvider(plugin).process(ip, name);
        }, true, antiBotManager.isSomeModeOnline() ? 1000L : 0L);
    }

    public int getUnderVerificationSize() {
        return underVerification.size();
    }

    public List<String> getIPSUnderVerification() {
        return underVerification;
    }
}

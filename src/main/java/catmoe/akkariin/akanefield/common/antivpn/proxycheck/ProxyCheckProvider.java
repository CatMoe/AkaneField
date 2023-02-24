package catmoe.akkariin.akanefield.common.antivpn.proxycheck;

import catmoe.akkariin.akanefield.common.objects.profile.BlackListReason;
import catmoe.akkariin.akanefield.common.objects.profile.BlackListProfile;
import catmoe.akkariin.akanefield.common.antivpn.VPNProvider;
import catmoe.akkariin.akanefield.common.antivpn.proxycheck.result.ProxyResults;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.tasks.TimedWhitelistTask;
import catmoe.akkariin.akanefield.common.utils.MessageManager;

public class ProxyCheckProvider implements VPNProvider {
    private final IAntiBotPlugin plugin;

    public ProxyCheckProvider(IAntiBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getID() {
        return "ProxyCheck";
    }

    @Override
    public void process(String ip, String name) {
        VPNResults res = getResults(ip);
        if (res == null)
            return;

        if (res.isProxy) {
            BlackListProfile profile = plugin.getAntiBotManager().getBlackListService().blacklistAndGet(ip,
                    BlackListReason.VPN, name);
            plugin.disconnect(ip, MessageManager.getBlacklistedMessage(profile));
        } else {
            plugin.getAntiBotManager().getWhitelistService().whitelist(ip);
            plugin.scheduleDelayedTask(new TimedWhitelistTask(plugin, ip, 30));
        }
    }

    @Override
    public String getCountry(String ip, String name) {
        VPNResults results = getResults(ip);
        return results == null ? "" : results.country;
    }

    private VPNResults getResults(String ip) {
        ConnectionCheck connectionCheck = new ConnectionCheck();
        try {
            ProxyResults results = connectionCheck.getAndMapResults(ip.replace("/", ""));
            if (results == null) {
                plugin.getLogHelper().warn("Your API key has reached the daily limit or is not valid!");
                return null;
            }
            plugin.getLogHelper().debug("ConnectionCheckerService --> checked " + ip + " result " + results.getProxy());
            return new VPNResults(results.getIsoCode(), results.getProxy().equals("yes"));
        } catch (Exception e) {
            plugin.getLogHelper().warn("Your API key has reached the daily limit or is not valid!");
            return null;
        }

    }

    private static class VPNResults {
        private String country;
        private boolean isProxy;

        public VPNResults(String country, boolean isProxy) {
            this.country = country;
            this.isProxy = isProxy;
        }
    }
}

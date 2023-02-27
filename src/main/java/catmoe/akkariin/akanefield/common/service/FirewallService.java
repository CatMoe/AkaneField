package catmoe.akkariin.akanefield.common.service;

import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.IConfiguration;
import catmoe.akkariin.akanefield.common.objects.FancyPair;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.common.utils.RuntimeUtil;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class FirewallService {
    private final IAntiBotPlugin plugin;
    private final IConfiguration configuration;

    private final String ipSetID;
    private final int timeout;
    private final boolean resetOnBlackListClear;
    private final String blacklistCommand;
    private final List<String> unBlacklistCommand;
    private boolean isEnabled;

    private final Queue<String> IPQueue;
    private long blacklisted;

    public FirewallService(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.configuration = plugin.getConfigYml();

        this.ipSetID = configuration.getString("firewall.ip-set-id");
        this.timeout = configuration.getInt("firewall.timeout");
        this.resetOnBlackListClear = configuration.getBoolean("firewall.reset-on-blacklist-clear");
        this.blacklistCommand = configuration.getString("firewall.blacklist-command");
        this.unBlacklistCommand = configuration.getStringList("firewall.un-blacklist-command");
        this.isEnabled = configuration.getBoolean("firewall.enabled");

        this.IPQueue = new ArrayDeque<>();
        this.blacklisted = 0;

        plugin.scheduleRepeatingTask(() -> {
            String ip = IPQueue.poll();

            if (ip == null || IPQueue.size() == 0) {
                return;
            }

            plugin.getLogHelper().debug("[FIREWALL] &c" + ip + " &fhas been firewalled!");
            blacklisted++;
            RuntimeUtil.execute(getBlackListCommand(ip));
        }, true, 20L);
    }

    public void enable() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            this.isEnabled = false;
            return;
        }
        if (!isEnabled)
            return;
        plugin.getLogHelper().info(MessageManager.getMessage("firewall.hooking"));

        FancyPair<Boolean, String> checkInstallation = checkInstallation();
        if (!checkInstallation.getElementA()) {
            plugin.getLogHelper().error(MessageManager.getMessage("firewall.not-install"));
            plugin.getLogHelper().error(checkInstallation.getElementB());
            isEnabled = false;
            return;
        }

        if (!isEnabled)
            return;
        setupFirewall();
        plugin.getLogHelper().info(MessageManager.getMessage("firewall.hooked"));

        try {
            plugin.getLogHelper().info(MessageManager.getMessage("firewall.processing"));

            int processed = 0;
            int percentCheck = 10;
            int total = plugin.getAntiBotManager().getBlackListService().getBlackListedIPS().size();

            for (String p : plugin.getAntiBotManager().getBlackListService().getBlackListedIPS()) {
                RuntimeUtil.execute(getBlackListCommand(p));
                processed++;

                int percent = Math.round((float) processed / total * 100);
                if (percent >= percentCheck && total > 500) {
                    percentCheck += 10;
                    plugin.getLogHelper().info("&b进度: &f" + percent + "&b%");
                }
            }

            plugin.getLogHelper().info(MessageManager.getMessage("firewall.enable"));
        } catch (Exception e) {
            plugin.getLogHelper().error(MessageManager.getMessage("firewall.exception"));
        }
    }

    public void shutDownFirewall() {
        if (!isEnabled)
            return;
        for (String cmd : configuration.getStringList("firewall.shutdown-commands")) {
            String cmd1 = cmd.replace("%t%", String.valueOf(timeout)).replace("%options%", "maxelem 200000 timeout")
                    .replace("%set%", ipSetID);
            RuntimeUtil.execute(cmd1);
        }
    }

    public void firewall(String ip) {
        if (!isEnabled)
            return;
        IPQueue.add(ip);
    }

    public void dropIP(String ip) {
        if (!isEnabled)
            return;
        unBlacklist(ip);
    }

    public void drop() {
        if (!isEnabled)
            return;
        if (!resetOnBlackListClear)
            return;
        plugin.runTask(() -> RuntimeUtil.execute("ipset flush " + ipSetID), true);
        blacklisted = 0;
    }

    public String getFirewallStatus() {
        return isEnabled ? MessageManager.getMessage("firewall.enabled")
                : MessageManager.getMessage("firewall.disabled");
    }

    public int getIPQueue() {
        return IPQueue.size();
    }

    public long getBlacklistedIP() {
        return blacklisted;
    }

    private void setupFirewall() {
        RuntimeUtil.execute("ipset destroy " + ipSetID);
        for (String str : configuration.getStringList("firewall.setup-commands")) {
            String cmd = str.replace("%t%", String.valueOf(timeout)).replace("%options%", "maxelem 200000 timeout")
                    .replace("%set%", ipSetID);
            RuntimeUtil.execute(cmd);
        }
    }

    private FancyPair<Boolean, String> checkInstallation() {
        String tables = RuntimeUtil.executeAndGetOutput("iptables --version");
        String ipset = RuntimeUtil.executeAndGetOutput("ipset --version");
        return new FancyPair<>(tables.contains("iptables v") && ipset.contains("ipset v"), ipset);
    }

    private String getBlackListCommand(String ip) {
        ip = ip.replace("/", "");

        String cmd = "";
        cmd = blacklistCommand.replace("%ip%", ip).replace("%t%", String.valueOf(timeout))
                .replace("%options%", "timeout").replace("%set%", ipSetID);

        return cmd;
    }

    private void unBlacklist(String ip) {
        ip = ip.replace("/", "");
        for (String str : unBlacklistCommand) {
            String cmd = str.replace("%ip%", ip).replace("%t%", String.valueOf(timeout)).replace("%options%", "timeout")
                    .replace("%set%", ipSetID);
            RuntimeUtil.execute(cmd);
        }
    }
}

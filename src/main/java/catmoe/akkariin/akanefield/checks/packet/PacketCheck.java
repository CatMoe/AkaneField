package catmoe.akkariin.akanefield.checks.packet;

import catmoe.akkariin.akanefield.common.objects.profile.BlackListReason;
import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.service.BlackListService;
import catmoe.akkariin.akanefield.common.service.WhitelistService;
import catmoe.akkariin.akanefield.common.utils.ConfigManager;
import net.md_5.bungee.api.ProxyServer;

import java.util.*;

public class PacketCheck {
    private final IAntiBotPlugin iAntiBotPlugin;
    private final Set<String> joined;
    private final Set<String> packetReceived;
    private final Set<String> suspected;
    private final IAntiBotManager antibotManager;
    private final BlackListService blacklist;
    private final WhitelistService whitelistService;

    public PacketCheck(IAntiBotPlugin plugin) {
        this.iAntiBotPlugin = plugin;
        this.joined = new HashSet<>();
        this.packetReceived = new HashSet<>();
        this.suspected = new HashSet<>();
        this.antibotManager = plugin.getAntiBotManager();
        this.blacklist = plugin.getAntiBotManager().getBlackListService();
        this.whitelistService = plugin.getAntiBotManager().getWhitelistService();

        List<String> invalidPlugins = Arrays.asList("Geyser-BungeeCord", "Protocolize", "HAProxyDetector", "floodgate",
                "MultiLogin");

        for (String invalidPlugin : invalidPlugins) {
            if (ProxyServer.getInstance().getPluginManager().getPlugin(invalidPlugin) != null) {
                iAntiBotPlugin.getLogHelper().warn(
                        "我们无法确认AkaneField对 "
                                + invalidPlugin + " 插件的兼容性 如果您遇到任何问题 请报告给CatMoe");
                ConfigManager.getPacketCheckConfig().setEnabled(false);
            }
        }

        if (isEnabled()) {
            loadTask();
            plugin.getLogHelper().debug("Loaded " + this.getClass().getSimpleName() + "!");
        }
    }

    public void onUnLogin(String ip) {
        joined.remove(ip);
        packetReceived.remove(ip);
        suspected.remove(ip);
    }

    public void registerJoin(String ip) {
        if (isEnabled() && !whitelistService.isWhitelisted(ip)) {
            joined.add(ip);
            checkForAttack();
        }
    }

    public void registerPacket(String ip) {
        if (isEnabled() && !whitelistService.isWhitelisted(ip)) {
            packetReceived.add(ip);
        }
    }

    public void checkForAttack() {
        iAntiBotPlugin.scheduleDelayedTask(() -> {
            joined.forEach(user -> {
                if (!packetReceived.contains(user)) {
                    suspected.add(user);
                }
            });

            suspected.removeIf(packetReceived::contains);

            if (suspected.size() >= ConfigManager.getPacketCheckConfig().getTrigger()) {
                iAntiBotPlugin.getLogHelper().debug("Packet Check Executed!");
                for (String ip : new ArrayList<>(suspected)) {
                    if (ConfigManager.getPacketCheckConfig().isBlacklist()) {
                        blacklist.blacklist(ip, BlackListReason.STRANGE_PLAYER);
                    }
                }
                if (ConfigManager.getPacketCheckConfig().isEnableAntiBotMode()) {
                    antibotManager.enableSlowAntiBotMode();
                }
                suspected.clear();
                iAntiBotPlugin.getLogHelper().debug("[UAB DEBUG] Detected attack on PacketCheck!");
            }
        }, false, 2500L);
    }

    private boolean isEnabled() {
        return ConfigManager.getPacketCheckConfig().isEnabled();
    }

    private void loadTask() {
        iAntiBotPlugin.scheduleRepeatingTask(() -> {
            if (!antibotManager.isAntiBotModeEnabled()) {
                return;
            }
            joined.clear();
            packetReceived.clear();
            suspected.clear();
        }, false, 1000L * ConfigManager.taskManagerClearPacket);
    }
}

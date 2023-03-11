package catmoe.fallencrystal.akanefield.checks.packet;

import net.md_5.bungee.api.ProxyServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.objects.profile.BlackListReason;
import catmoe.fallencrystal.akanefield.common.service.BlackListService;
import catmoe.fallencrystal.akanefield.common.service.WhitelistService;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;

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
        /**
         * List<String> invalidPlugins = Arrays.asList("Geyser-BungeeCord",
         * "HAProxyDetector", "floodgate");
         * List<String> needLatestPlugins = Arrays.asList("Protocolize", "MultiLogin");
         * List<String> unSupportedProxyPlugins = Arrays.asList("HAProxyDetector");
         * for (String invalidPlugin : invalidPlugins) {
         * if (ProxyServer.getInstance().getPluginManager().getPlugin(invalidPlugin) !=
         * null) {
         * iAntiBotPlugin.getLogHelper().warn("我们无法确认对插件 " + invalidPlugin + " 兼容性
         * 如果您遇到任何问题 请报告给CatMoe");
         * }
         * }
         * for (String needLatestPlugin : needLatestPlugins) {
         * if (ProxyServer.getInstance().getPluginManager().getPlugin(needLatestPlugin)
         * != null) {
         * iAntiBotPlugin.getLogHelper().info("请保持插件 " + needLatestPlugin + " 为最新版
         * 如因旧版造成的问题概不负责.");
         * }
         * }
         */
        List<String> needTipPlugins = Arrays.asList("Geyser-BungeeCord", "HAProxyDetector", "floodgate", "Protocolize",
                "MultiLogin", "HAProxyDetector", "AntiAttackRL", "nAntiBot", "JH_AntiBot");
        for (String needTipPlugin : needTipPlugins) {
            if (ProxyServer.getInstance().getPluginManager().getPlugin(needTipPlugin) != null) {
                List<String> invalidPlugins = Arrays.asList("Geyser-BungeeCord", "HAProxyDetector", "floodgate");
                List<String> needLatestPlugins = Arrays.asList("Protocolize", "MultiLogin");
                List<String> modifiedPiplinePlugins = Arrays.asList("HAProxyDetector", "AntiAttackRL", "nAntiBot",
                        "JH_AntiBot");
                for (String invalidPlugin : invalidPlugins) {
                    if (ProxyServer.getInstance().getPluginManager().getPlugin(invalidPlugin) != null) {
                        plugin.getLogHelper().warn("可能不支持的插件 \"" + invalidPlugin + "\" 如果您遇到任何兼容问题 请及时向CatMoe报告问题.");
                    }
                }
                for (String needLatestPlugin : needLatestPlugins) {
                    if (ProxyServer.getInstance().getPluginManager().getPlugin(needLatestPlugin) != null) {
                        plugin.getLogHelper().info("请保持插件 \"" + needLatestPlugin + "\" 为最新版本以兼容AkaneField.");
                    }
                }
                for (String modifiedPipline : modifiedPiplinePlugins) {
                    if (ProxyServer.getInstance().getPluginManager().getPlugin(modifiedPipline) != null) {
                        plugin.getLogHelper().error("");
                        plugin.getLogHelper().error("请卸载插件 " + modifiedPipline);
                        plugin.getLogHelper().error("此插件具有修改侦听管道的危险行为");
                        plugin.getLogHelper().error("此插件也不在我们的支持范围内!");
                        plugin.getLogHelper().error("如果您继续使用此插件 CatMoe 不会为您的任何问题负责.");
                        plugin.getLogHelper().error("此插件可能是有关网络协议的插件 (e.x.HAProxy, 其它反机器人插件)");
                        plugin.getLogHelper().error("");
                        plugin.getLogHelper().error("您可能觉得AkaneField需要搭配其它反机器人使用? 我不这么觉得");
                        plugin.getLogHelper().error("我不修改侦听为了兼容性是有原因的 你真的要打破这个吗?");
                        plugin.getLogHelper().error("");
                    }
                }
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

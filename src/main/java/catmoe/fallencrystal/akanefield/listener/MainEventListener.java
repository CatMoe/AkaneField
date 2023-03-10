package catmoe.fallencrystal.akanefield.listener;

import catmoe.fallencrystal.akanefield.Notificator;
import catmoe.fallencrystal.akanefield.checks.AuthCheckReloaded;
import catmoe.fallencrystal.akanefield.checks.packet.PacketCheck;
import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.checks.*;
import catmoe.fallencrystal.akanefield.common.objects.profile.BlackListReason;
import catmoe.fallencrystal.akanefield.common.service.*;
import catmoe.fallencrystal.akanefield.common.tasks.AutoWhitelistTask;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.utils.ComponentBuilder;
import catmoe.fallencrystal.akanefield.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.SettingsChangedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class MainEventListener implements Listener {
    private final IAntiBotPlugin plugin;
    private final IAntiBotManager antiBotManager;
    private final QueueService queueService;
    private final WhitelistService whitelistService;
    private final BlackListService blackListService;
    private final FirstJoinCheck firstJoinCheck;
    private final NameChangerCheck nameChangerCheck;
    private final SuperJoinCheck superJoinCheck;
    private final AuthCheckReloaded authCheck;
    private final PacketCheck packetCheck;
    private final AccountCheck accountCheck;
    private final SlowJoinCheck slowJoinCheck;
    private final LegalNameCheck legalNameCheck;
    private final InvalidNameCheck invalidNameCheck;
    private final RegisterCheck registerCheck;
    private final VPNService VPNService;
    private final UserDataService userDataService;

    public MainEventListener(IAntiBotPlugin antiBotPlugin) {
        this.plugin = antiBotPlugin;
        this.antiBotManager = antiBotPlugin.getAntiBotManager();
        this.queueService = antiBotManager.getQueueService();
        this.whitelistService = antiBotManager.getWhitelistService();
        this.blackListService = antiBotManager.getBlackListService();
        this.firstJoinCheck = new FirstJoinCheck(antiBotPlugin);
        this.nameChangerCheck = new NameChangerCheck(antiBotPlugin);
        this.superJoinCheck = new SuperJoinCheck(antiBotPlugin);
        this.authCheck = new AuthCheckReloaded(antiBotPlugin);
        this.packetCheck = new PacketCheck(antiBotPlugin);
        this.accountCheck = new AccountCheck(antiBotPlugin);
        this.slowJoinCheck = new SlowJoinCheck(antiBotPlugin);
        this.legalNameCheck = new LegalNameCheck(antiBotPlugin);
        this.invalidNameCheck = new InvalidNameCheck(plugin);
        this.registerCheck = new RegisterCheck(plugin);
        this.VPNService = plugin.getVPNService();
        this.userDataService = plugin.getUserDataService();
    }

    @EventHandler(priority = -128)
    public void onPreLoginEvent(PreLoginEvent e) {
        // e.registerIntent(AkaneFieldProxy.getInstance());
        String ip = Utils.getIP(e.getConnection());
        String name = e.getConnection().getName();
        int totals = blackListService.size() + queueService.size();
        if (blackListService.size() != 0 && totals != 0) {
            ServerUtil.blacklistPercentage = Math.round((float) blackListService.size() / totals * 100);
        }
        antiBotManager.increaseJoinPerSecond();

        //
        // BlackList & Whitelist Checks
        //
        if (blackListService.isBlackListed(ip)) {
            e.setCancelReason(blacklistMSG(ip));
            e.setCancelled(true);
            return;
        }
        if (whitelistService.isWhitelisted(ip)) {
            return;
        }

        //
        // AntiBotMode Enable
        //
        if (antiBotManager.getSpeedJoinPerSecond() >= ConfigManager.antiBotModeTrigger) {
            if (!antiBotManager.isAntiBotModeEnabled()) {
                antiBotManager.enableAntiBotMode();
                e.setCancelReason(ComponentBuilder.buildColorized(
                        MessageManager.getAntiBotModeMessage(String.valueOf(ConfigManager.authPercent),
                                String.valueOf(ServerUtil.blacklistPercentage))));
                e.setCancelled(true);
                return;
            }
        }
        //
        // NameChangerCheck
        //
        if (nameChangerCheck.isDenied(ip, name)) {
            blackListService.blacklist(ip, BlackListReason.TOO_MUCH_NAMES, name);
            e.setCancelReason(blacklistMSG(ip));
            e.setCancelled(true);
            return;
        }
        //
        // Queue Service
        //
        if (!queueService.isQueued(ip) && !blackListService.isBlackListed(ip) && !whitelistService.isWhitelisted(ip)) {
            queueService.queue(ip);
        }

        //
        // Legal Name Check
        //
        if (legalNameCheck.isDenied(ip, name)) {
            e.setCancelReason(blacklistMSG(ip));
            e.setCancelled(true);
            return;
        }
        //
        // FirstJoinCheck
        //
        if (firstJoinCheck.isDenied(ip, name)) {
            e.setCancelReason(ComponentBuilder.buildColorized(MessageManager.getFirstJoinMessage(null)));
            e.setCancelled(true);
            return;
        }
        //
        // Invalid Name Check
        //
        if (invalidNameCheck.isDenied(ip, name)) {
            e.setCancelReason(blacklistMSG(ip));
            e.setCancelled(true);
            return;
        }
        //
        // SuperJoinCheck
        //
        if (superJoinCheck.isDenied(ip, name)) {
            blackListService.blacklist(ip, BlackListReason.TOO_MUCH_JOINS, name);
            e.setCancelReason(blacklistMSG(ip));
            e.setCancelled(true);
            return;
        }
        //
        // Auth Check
        //
        if (ServerUtil.blacklistPercentage >= ConfigManager.authPercent && antiBotManager.isAntiBotModeEnabled()) {
            authCheck.onJoin(e, ip);
            return;
        }
        //
        // AntiBotMode Normal
        //
        if (antiBotManager.isAntiBotModeEnabled() || antiBotManager.isSlowAntiBotModeEnabled()) {
            e.setCancelReason(ComponentBuilder.buildColorized(
                    MessageManager.getAntiBotModeMessage(String.valueOf(ConfigManager.authPercent),
                            String.valueOf(ServerUtil.blacklistPercentage))));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPostLoginEvent(PostLoginEvent e) {
        ProxiedPlayer player = e.getPlayer();
        String nickname = player.getName();
        String ip = Utils.getIP(player);
        //
        // Packet Check
        //
        packetCheck.registerJoin(ip);
        //
        // connection profiles
        //
        userDataService.registerJoin(ip, nickname);
        //
        // Account Check
        //
        if (accountCheck.isDenied(ip, nickname)) {
            plugin.disconnect(ip, MessageManager.getAccountOnlineMessage());
            return;
        }
        //
        // SlowJoin check
        //
        if (slowJoinCheck.isDenied(ip, nickname)) {
            blackListService.blacklist(ip, BlackListReason.STRANGE_PLAYER, nickname);
            return;
        }
        // If isn't whitelisted
        if (!antiBotManager.getWhitelistService().isWhitelisted(ip)) {
            // Add to last join
            antiBotManager.getJoinCache().addJoined(ip);
            // Auto Whitelist Task
            plugin.scheduleDelayedTask(new AutoWhitelistTask(plugin, ip), false,
                    1000L * ConfigManager.playtimeForWhitelist * 60L);
            // Remove from JoinCache after 30 Seconds
            plugin.scheduleDelayedTask(() -> antiBotManager.getJoinCache().removeJoined(ip), false, 1000L * 30);
            //
            // Connection check (ProxyCheck.io or ip-api.com)
            //
            if (!player.hasPermission("akanefield.bypass.vpn")) {
                VPNService.submitIP(ip, nickname);
            }
        }
        // Notification
        if (player.hasPermission("akanefield.notification.automatic") && antiBotManager.isSomeModeOnline()) {
            Notificator.automaticNotification(player);
        }
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        String ip = Utils.getIP(e.getSender());
        String nickname = ((ProxiedPlayer) e.getSender()).getName();

        //
        // Register Check
        //
        registerCheck.onChat(ip, nickname, e.getMessage());
    }

    @EventHandler
    public void onPing(ProxyPingEvent e) {
        String ip = Utils.getIP(e.getConnection());
        //
        // Auth Check Ping Action
        //
        if (ServerUtil.blacklistPercentage >= ConfigManager.authPercent && antiBotManager.isAntiBotModeEnabled()) {
            authCheck.onPing(e, ip);
        }
    }

    @EventHandler
    public void onUnLogin(PlayerDisconnectEvent e) {
        String ip = Utils.getIP(e.getPlayer());
        //
        // Packet Check
        //
        packetCheck.onUnLogin(ip);
        //
        // Account Check
        //
        accountCheck.onDisconnect(ip, e.getPlayer().getName());
        //
        // connection profiles
        //
        userDataService.registerQuit(ip);
        //
        // Invalid name check
        //
        invalidNameCheck.onDisconnect(ip, e.getPlayer().getName());
        //
        // Register check
        //
        registerCheck.onDisconnect(ip, e.getPlayer().getName());
    }

    @EventHandler
    public void onSettings(SettingsChangedEvent e) {
        String ip = Utils.getIP(e.getPlayer());
        //
        // PacketCheck
        //
        packetCheck.registerPacket(ip);
    }

    private BaseComponent blacklistMSG(String ip) {
        return ComponentBuilder.buildColorized(MessageManager.getBlacklistedMessage(blackListService.getProfile(ip)));
    }
}

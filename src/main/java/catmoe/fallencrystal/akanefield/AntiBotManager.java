package catmoe.fallencrystal.akanefield;

import java.util.ArrayList;
import java.util.List;

import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.ModeType;
import catmoe.fallencrystal.akanefield.common.cache.JoinCache;
import catmoe.fallencrystal.akanefield.common.detectors.AttackDurationDetector;
import catmoe.fallencrystal.akanefield.common.helper.LogHelper;
import catmoe.fallencrystal.akanefield.common.service.BlackListService;
import catmoe.fallencrystal.akanefield.common.service.QueueService;
import catmoe.fallencrystal.akanefield.common.service.VPNService;
import catmoe.fallencrystal.akanefield.common.service.WhitelistService;
import catmoe.fallencrystal.akanefield.common.thread.DynamicCounterThread;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.common.utils.TimeUtil;
import catmoe.fallencrystal.akanefield.event.ModeEnableEvent;
import catmoe.fallencrystal.akanefield.task.ModeDisableTask;
import catmoe.fallencrystal.akanefield.utils.EventCaller;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AntiBotManager implements IAntiBotManager {
    private final IAntiBotPlugin iAntiBotPlugin;
    private final DynamicCounterThread joinPerSecond;
    private final DynamicCounterThread pingPerSecond;
    private final DynamicCounterThread packetPerSecond;
    private final DynamicCounterThread connectionPerSecond;
    private final AttackDurationDetector attackDurationDetector;
    private final BlackListService blackListService;
    private final WhitelistService whitelistService;
    private final QueueService queueService;
    private ModeType modeType;
    private boolean isAntiBotModeOnline;
    private boolean isSlowAntiBotModeOnline;
    private boolean isPacketModeEnabled;
    private boolean isPingModeEnabled;
    private final LogHelper logHelper;
    private final JoinCache joinCache;
    private final VPNService VPNService;

    public AntiBotManager(IAntiBotPlugin plugin) {
        this.iAntiBotPlugin = plugin;
        this.logHelper = plugin.getLogHelper();
        this.joinPerSecond = new DynamicCounterThread(plugin);
        this.pingPerSecond = new DynamicCounterThread(plugin);
        this.packetPerSecond = new DynamicCounterThread(plugin);
        this.connectionPerSecond = new DynamicCounterThread(plugin);
        this.attackDurationDetector = new AttackDurationDetector(plugin);
        this.queueService = new QueueService();
        this.blackListService = new BlackListService(plugin, queueService, plugin.getBlackList(), logHelper);
        this.whitelistService = new WhitelistService(queueService, plugin.getWhitelist(), logHelper);
        this.modeType = ModeType.Block;
        this.isAntiBotModeOnline = false;
        this.isSlowAntiBotModeOnline = false;
        this.isPacketModeEnabled = false;
        this.isPingModeEnabled = false;
        this.joinCache = new JoinCache();
        this.VPNService = plugin.getVPNService();
    }

    @Override
    public long getJoinPerSecond() {
        return joinPerSecond.getSlowCount();
    }

    @Override
    public long getSpeedJoinPerSecond() {
        return joinPerSecond.getSpeedCount();
    }

    @Override
    public long getPingPerSecond() {
        return pingPerSecond.getSlowCount();
    }

    @Override
    public long getPacketPerSecond() {
        return packetPerSecond.getSlowCount();
    }

    @Override
    public long getConnectionPerSecond() {
        return connectionPerSecond.getSlowCount();
    }

    @Override
    public long getAttackDuration() {
        return attackDurationDetector.getAttackDuration();
    }

    @Override
    public DynamicCounterThread getDynamicJoins() {
        return joinPerSecond;
    }

    @Override
    public DynamicCounterThread getDynamicPings() {
        return pingPerSecond;
    }

    @Override
    public DynamicCounterThread getDynamicPackets() {
        return packetPerSecond;
    }

    @Override
    public BlackListService getBlackListService() {
        return blackListService;
    }

    @Override
    public QueueService getQueueService() {
        return queueService;
    }

    @Override
    public WhitelistService getWhitelistService() {
        return whitelistService;
    }

    @Override
    public ModeType getModeType() {
        return modeType;
    }

    @Override
    public void setModeType(ModeType type) {
        this.modeType = type;

        if (type != ModeType.Block) {
            this.modeType = ModeType.Block;
        }
    }

    @Override
    public void disableAll() {
        this.isAntiBotModeOnline = false;
        this.isSlowAntiBotModeOnline = false;
        this.isPacketModeEnabled = false;
        this.isPingModeEnabled = false;
        this.modeType = ModeType.Block;
    }

    @Override
    public void disableMode(ModeType type) {
        if (type.equals(ModeType.FastJoin)) {
            this.isAntiBotModeOnline = false;
        }
        if (type.equals(ModeType.SlowJoin)) {
            this.isSlowAntiBotModeOnline = false;
        }
        if (type.equals(ModeType.Firewall)) {
            this.isPacketModeEnabled = false;
        }
        if (type.equals(ModeType.Motd)) {
            this.isPingModeEnabled = false;
        }
        if (type != ModeType.Block) {
            this.modeType = ModeType.Block;
        }
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.Block));
    }

    @Override
    public boolean isSomeModeOnline() {
        return isAntiBotModeOnline || isSlowAntiBotModeOnline || isPacketModeEnabled || isPingModeEnabled;
    }

    @Override
    public void increaseJoinPerSecond() {
        joinPerSecond.increase();
        increaseConnectionPerSecond();
    }

    @Override
    public void increasePingPerSecond() {
        pingPerSecond.increase();
        increaseConnectionPerSecond();
    }

    @Override
    public void increasePacketPerSecond() {
        packetPerSecond.increase();
        increaseConnectionPerSecond();
    }

    @Override
    public void increaseConnectionPerSecond() {
        connectionPerSecond.increase();
    }

    @Override
    public boolean isAntiBotModeEnabled() {
        return isAntiBotModeOnline;
    }

    @Override
    public boolean isSlowAntiBotModeEnabled() {
        return isSlowAntiBotModeOnline;
    }

    @Override
    public boolean isPacketModeEnabled() {
        return isPacketModeEnabled;
    }

    @Override
    public boolean isPingModeEnabled() {
        return isPingModeEnabled;
    }

    @Override
    public void enableAntiBotMode() {
        setModeType(ModeType.FastJoin);
        isAntiBotModeOnline = true;
        isSlowAntiBotModeOnline = false;
        isPingModeEnabled = false;
        isPacketModeEnabled = false;
        iAntiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(iAntiBotPlugin, ModeType.FastJoin),
                false, 1000L * ConfigManager.antiBotModeKeep);
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.FastJoin));
    }

    @Override
    public void enableSlowAntiBotMode() {
        setModeType(ModeType.SlowJoin);
        isAntiBotModeOnline = false;
        isSlowAntiBotModeOnline = true;
        isPingModeEnabled = false;
        isPacketModeEnabled = false;
        iAntiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(iAntiBotPlugin, ModeType.SlowJoin),
                false, 1000L * ConfigManager.slowAntibotModeKeep);
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.SlowJoin));
    }

    @Override
    public void enablePacketMode() {
        setModeType(ModeType.Firewall);
        isAntiBotModeOnline = false;
        isSlowAntiBotModeOnline = false;
        isPingModeEnabled = false;
        isPacketModeEnabled = true;
        iAntiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(iAntiBotPlugin, ModeType.Firewall),
                false, 1000L * ConfigManager.packetModeKeep);
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.Firewall));
    }

    @Override
    public void enablePingMode() {
        setModeType(ModeType.Motd);
        isAntiBotModeOnline = false;
        isSlowAntiBotModeOnline = false;
        isPingModeEnabled = true;
        isPacketModeEnabled = false;
        iAntiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(iAntiBotPlugin, ModeType.Motd),
                false, 1000L * ConfigManager.pingModeKeep);
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.Motd));
    }

    // || isPingModeEnabled || isSlowAntiBotModeOnline
    @Override
    public void dispatchConsoleAttackMessage() {
        if (isAntiBotModeOnline) {
            logHelper.info(replaceInfo(MessageManager.actionbarBlock.replace("%prefix%", "")));
        } else {
            if (isPacketModeEnabled) {
                logHelper.info(replaceInfo(MessageManager.actionbarFirewall.replace("%prefix%", "")));
            }
        }
    }

    @Override
    public boolean canDisable(ModeType modeType) {
        if (modeType.equals(ModeType.FastJoin) || modeType.equals(ModeType.SlowJoin)) {
            return joinPerSecond.getSlowCount() <= ConfigManager.antiBotModeTrigger;
        }
        if (modeType.equals(ModeType.Motd)) {
            return pingPerSecond.getSlowCount() <= ConfigManager.pingModeTrigger;
        }
        if (modeType.equals(ModeType.Firewall)) {
            return packetPerSecond.getSlowCount() < ConfigManager.packetModeTrigger;
        }
        return false;
    }

    @Override
    public JoinCache getJoinCache() {
        return joinCache;
    }

    public void UnderAttackNotification(ProxiedPlayer player) {
        final List<ProxiedPlayer> titles = new ArrayList<>();
        try {
            if (getAttackDuration() == 1) {
                titles.add(player);
                Title t = ProxyServer.getInstance().createTitle();
                t.title(new TextComponent(ServerUtil.colorize(MessageManager.getMessage("underattack.title.title"))));
                t.subTitle(new TextComponent(
                        ServerUtil.colorize(MessageManager.getMessage("underattack.title.subtitle"))));
                t.stay(MessageManager.attackNotificationTitleStay);
                t.fadeIn(MessageManager.attackNotificationTitleFadeIn);
                t.fadeOut(MessageManager.attackNotificationTitleFadeOut);
            } else {
                titles.remove(player);
            }
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public String replaceInfo(String str) {
        return str
                .replace("%bots%", String.valueOf(joinPerSecond.getSlowCount()))
                .replace("%pings%", String.valueOf(pingPerSecond.getSlowCount()))
                .replace("%queue%", String.valueOf(queueService.size()))
                .replace("%whitelist%", String.valueOf(whitelistService.size()))
                .replace("%blacklist%", String.valueOf(blackListService.size()))
                .replace("%firewall-blacklist%", String.valueOf(iAntiBotPlugin.getFirewallService().getBlacklistedIP()))
                .replace("%firewall-queue%", String.valueOf(iAntiBotPlugin.getFirewallService().getIPQueue()))
                .replace("%target%", String.valueOf(iAntiBotPlugin.getFirewallService().getFirewallStatus()))
                .replace("%packets%", String.valueOf(packetPerSecond.getSlowCount()))
                .replace("%totalbots%", String.valueOf(joinPerSecond.getTotal()))
                .replace("%totalpings%", String.valueOf(pingPerSecond.getTotal()))
                .replace("%totalpackets%", String.valueOf(packetPerSecond.getTotal()))
                .replace("%animation%", iAntiBotPlugin.getAnimationThread().getEmote())
                .replace("%prefix%", MessageManager.prefix)
                .replace("%underverification%", String.valueOf(VPNService.getUnderVerificationSize()))
                .replace("%duration%", TimeUtil.formatSeconds(attackDurationDetector.getAttackDuration()))
                .replace("%totalblocks%", String.valueOf(connectionPerSecond.getTotal()))
                .replace("%cps%", String.valueOf(connectionPerSecond.getSlowCount()))
                // 添加了缓存办法 原有的属性将被弃用
                // .replace("%proccpu%",
                // String.valueOf(CPUMonitor.getRoundCurrentProcessCpuLoad()))
                // .replace("%syscpu%",
                // String.valueOf(CPUMonitor.getRoundCurrentSystemCpuLoad()))
                .replace("%connection%", String.valueOf(connectionPerSecond.getSpeedCount()));
    }
}
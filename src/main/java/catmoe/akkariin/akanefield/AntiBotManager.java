package catmoe.akkariin.akanefield;

import catmoe.akkariin.akanefield.common.cache.JoinCache;
import catmoe.akkariin.akanefield.common.detectors.AttackDurationDetector;
import catmoe.akkariin.akanefield.common.helper.LogHelper;
import catmoe.akkariin.akanefield.common.ModeType;
import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.service.BlackListService;
import catmoe.akkariin.akanefield.common.service.VPNService;
import catmoe.akkariin.akanefield.common.service.QueueService;
import catmoe.akkariin.akanefield.common.service.WhitelistService;
import catmoe.akkariin.akanefield.common.thread.DynamicCounterThread;
import catmoe.akkariin.akanefield.common.utils.ConfigManger;
import catmoe.akkariin.akanefield.common.utils.Formatter;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.event.ModeEnableEvent;
import catmoe.akkariin.akanefield.task.ModeDisableTask;
import catmoe.akkariin.akanefield.utils.EventCaller;

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
        this.modeType = ModeType.OFFLINE;
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

        if (type != ModeType.OFFLINE) {
            this.modeType = ModeType.OFFLINE;
        }
    }

    @Override
    public void disableAll() {
        this.isAntiBotModeOnline = false;
        this.isSlowAntiBotModeOnline = false;
        this.isPacketModeEnabled = false;
        this.isPingModeEnabled = false;
        this.modeType = ModeType.OFFLINE;
    }

    @Override
    public void disableMode(ModeType type) {
        if (type.equals(ModeType.ANTIBOT)) {
            this.isAntiBotModeOnline = false;
        }
        if (type.equals(ModeType.SLOW)) {
            this.isSlowAntiBotModeOnline = false;
        }
        if (type.equals(ModeType.PACKETS)) {
            this.isPacketModeEnabled = false;
        }
        if (type.equals(ModeType.PING)) {
            this.isPingModeEnabled = false;
        }
        if (type != ModeType.OFFLINE) {
            this.modeType = ModeType.OFFLINE;
        }
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.OFFLINE));
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
        setModeType(ModeType.ANTIBOT);
        isAntiBotModeOnline = true;
        isSlowAntiBotModeOnline = false;
        isPingModeEnabled = false;
        isPacketModeEnabled = false;
        iAntiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(iAntiBotPlugin, ModeType.ANTIBOT),
                false, 1000L * ConfigManger.antiBotModeKeep);
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.ANTIBOT));
    }

    @Override
    public void enableSlowAntiBotMode() {
        setModeType(ModeType.SLOW);
        isAntiBotModeOnline = false;
        isSlowAntiBotModeOnline = true;
        isPingModeEnabled = false;
        isPacketModeEnabled = false;
        iAntiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(iAntiBotPlugin, ModeType.SLOW),
                false, 1000L * ConfigManger.slowAntibotModeKeep);
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.SLOW));
    }

    @Override
    public void enablePacketMode() {
        setModeType(ModeType.PACKETS);
        isAntiBotModeOnline = false;
        isSlowAntiBotModeOnline = false;
        isPingModeEnabled = false;
        isPacketModeEnabled = true;
        iAntiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(iAntiBotPlugin, ModeType.PACKETS),
                false, 1000L * ConfigManger.packetModeKeep);
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.PACKETS));
    }

    @Override
    public void enablePingMode() {
        setModeType(ModeType.PING);
        isAntiBotModeOnline = false;
        isSlowAntiBotModeOnline = false;
        isPingModeEnabled = true;
        isPacketModeEnabled = false;
        iAntiBotPlugin.scheduleDelayedTask(
                new ModeDisableTask(iAntiBotPlugin, ModeType.PING),
                false, 1000L * ConfigManger.pingModeKeep);
        EventCaller.call(new ModeEnableEvent(iAntiBotPlugin, ModeType.PING));
    }

    @Override
    public void dispatchConsoleAttackMessage() {
        if (isAntiBotModeOnline || isPingModeEnabled || isSlowAntiBotModeOnline) {
            logHelper.info(replaceInfo(MessageManager.actionbarAntiBotMode.replace("%prefix%", "")));
        } else {
            if (isPacketModeEnabled) {
                logHelper.info(replaceInfo(MessageManager.actionbarPackets.replace("%prefix%", "")));
            }
        }
    }

    @Override
    public boolean canDisable(ModeType modeType) {
        if (modeType.equals(ModeType.ANTIBOT) || modeType.equals(ModeType.SLOW)) {
            return joinPerSecond.getSlowCount() <= ConfigManger.antiBotModeTrigger;
        }
        if (modeType.equals(ModeType.PING)) {
            return pingPerSecond.getSlowCount() <= ConfigManger.pingModeTrigger;
        }
        if (modeType.equals(ModeType.PACKETS)) {
            return packetPerSecond.getSlowCount() < ConfigManger.packetModeTrigger;
        }
        return false;
    }

    @Override
    public JoinCache getJoinCache() {
        return joinCache;
    }

    @Override
    public String replaceInfo(String str) {
        return str
                .replace("%bots%", String.valueOf(joinPerSecond.getSlowCount()))
                .replace("%pings%", String.valueOf(pingPerSecond.getSlowCount()))
                .replace("%queue%", String.valueOf(queueService.size()))
                .replace("%whitelist%", String.valueOf(whitelistService.size()))
                .replace("%blacklist%", String.valueOf(blackListService.size()))
                .replace("%type%", String.valueOf(modeType.toString()))
                .replace("%packets%", String.valueOf(packetPerSecond.getSlowCount()))
                .replace("%totalbots%", String.valueOf(Formatter.format(joinPerSecond.getTotal())))
                .replace("%totalpings%", String.valueOf(Formatter.format(pingPerSecond.getTotal())))
                .replace("%totalpackets%", String.valueOf(Formatter.format(packetPerSecond.getTotal())))
                .replace("%latency%", iAntiBotPlugin.getLatencyThread().getLatency())
                .replace("%prefix%", iAntiBotPlugin.getAnimationThread().getEmote() + " " + MessageManager.prefix)
                .replace("%underverification%", String.valueOf(VPNService.getUnderVerificationSize()));
    }
}

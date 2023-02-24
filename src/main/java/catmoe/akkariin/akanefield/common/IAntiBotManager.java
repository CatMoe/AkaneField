package catmoe.akkariin.akanefield.common;

import catmoe.akkariin.akanefield.common.cache.JoinCache;
import catmoe.akkariin.akanefield.common.service.BlackListService;
import catmoe.akkariin.akanefield.common.service.QueueService;
import catmoe.akkariin.akanefield.common.service.WhitelistService;
import catmoe.akkariin.akanefield.common.thread.DynamicCounterThread;

public abstract interface IAntiBotManager {
    long getJoinPerSecond();

    long getSpeedJoinPerSecond();

    long getPingPerSecond();

    long getPacketPerSecond();

    long getConnectionPerSecond();

    long getAttackDuration();

    DynamicCounterThread getDynamicJoins();

    DynamicCounterThread getDynamicPings();

    DynamicCounterThread getDynamicPackets();

    BlackListService getBlackListService();

    QueueService getQueueService();

    WhitelistService getWhitelistService();

    ModeType getModeType();

    void setModeType(ModeType type);

    void disableAll();

    void disableMode(ModeType type);

    boolean isSomeModeOnline();

    void increaseJoinPerSecond();

    void increasePingPerSecond();

    void increasePacketPerSecond();

    void increaseConnectionPerSecond();

    boolean isAntiBotModeEnabled();

    boolean isSlowAntiBotModeEnabled();

    boolean isPacketModeEnabled();

    boolean isPingModeEnabled();

    void enableAntiBotMode();

    void enableSlowAntiBotMode();

    void enablePacketMode();

    void enablePingMode();

    void dispatchConsoleAttackMessage();

    boolean canDisable(ModeType modeType);

    JoinCache getJoinCache();

    String replaceInfo(String str);
}

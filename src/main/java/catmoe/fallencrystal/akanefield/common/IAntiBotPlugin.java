package catmoe.fallencrystal.akanefield.common;

import catmoe.fallencrystal.akanefield.common.core.AkaneFieldCore;
import catmoe.fallencrystal.akanefield.common.helper.LogHelper;
import catmoe.fallencrystal.akanefield.common.service.*;
import catmoe.fallencrystal.akanefield.common.thread.AnimationThread;
import catmoe.fallencrystal.akanefield.common.thread.LatencyThread;

public interface IAntiBotPlugin {
    void reload();

    void runTask(Runnable task, boolean isAsync);

    void runTask(FieldRunnable runnable);

    void scheduleDelayedTask(Runnable runnable, boolean async, long milliseconds);

    void scheduleDelayedTask(FieldRunnable runnable);

    void scheduleRepeatingTask(Runnable runnable, boolean async, long repeatMilliseconds);

    void scheduleRepeatingTask(FieldRunnable runnable);

    IConfiguration getConfigYml();

    IConfiguration getMessages();

    IConfiguration getWhitelist();

    IConfiguration getBlackList();

    IAntiBotManager getAntiBotManager();

    LatencyThread getLatencyThread();

    AnimationThread getAnimationThread();

    LogHelper getLogHelper();

    Class<?> getClassInstance();

    UserDataService getUserDataService();

    VPNService getVPNService();

    INotificator getNotificator();

    AkaneFieldCore getCore();

    FirewallService getFirewallService();

    boolean isConnected(String ip);

    String getVersion();

    void disconnect(String ip, String reasonNoColor);

    int getOnlineCount();

    boolean isRunning();

    AttackTrackerService getAttackTrackerService();
}

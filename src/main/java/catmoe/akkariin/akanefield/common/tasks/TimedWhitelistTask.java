package catmoe.akkariin.akanefield.common.tasks;

import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.FieldRunnable;

public class TimedWhitelistTask extends FieldRunnable {
    private final IAntiBotPlugin plugin;
    private final IAntiBotManager antiBotManager;
    private final String ip;
    private final int seconds;

    public TimedWhitelistTask(IAntiBotPlugin plugin, String ip, int seconds) {
        this.plugin = plugin;
        this.antiBotManager = plugin.getAntiBotManager();
        this.ip = ip;
        this.seconds = seconds;
    }

    @Override
    public void run() {
        antiBotManager.getWhitelistService().unWhitelist(ip);
        plugin.scheduleDelayedTask(new FieldRunnable() {
            @Override
            public boolean isAsync() {
                return false;
            }

            @Override
            public long getPeriod() {
                return 1000L * seconds;
            }

            @Override
            public void run() {
                if (plugin.isConnected(ip)) {
                    antiBotManager.getWhitelistService().whitelist(ip);
                }
            }
        });
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public long getPeriod() {
        return 2000L;
    }
}

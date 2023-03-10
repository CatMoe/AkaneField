package catmoe.fallencrystal.akanefield.common.tasks;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;

public class AutoWhitelistTask implements Runnable {

    private final IAntiBotPlugin antiBotPlugin;
    private final String ip;

    public AutoWhitelistTask(IAntiBotPlugin antiBotPlugin, String ip) {
        this.antiBotPlugin = antiBotPlugin;
        this.ip = ip;
    }

    @Override
    public void run() {
        if (antiBotPlugin.isConnected(ip)) {
            antiBotPlugin.getAntiBotManager().getWhitelistService().whitelist(ip);
        }
        antiBotPlugin.getAntiBotManager().getQueueService().removeQueue(ip);
    }
}

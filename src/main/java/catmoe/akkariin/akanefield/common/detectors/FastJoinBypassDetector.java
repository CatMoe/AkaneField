package catmoe.akkariin.akanefield.common.detectors;

import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.utils.ConfigManager;

public class FastJoinBypassDetector extends AbstractDetector {
    private final IAntiBotPlugin plugin;
    private int count;

    public FastJoinBypassDetector(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.count = 0;
    }

    public void registerJoin() {
        IAntiBotManager antiBotManager = plugin.getAntiBotManager();
        if (antiBotManager.isAntiBotModeEnabled() && antiBotManager.getPingPerSecond() > 50) {
            count++;
        }

        if (count > 2) {
            ConfigManager.incrementAuthCheckDifficulty();
            plugin.getLogHelper().debug("[BYPASS DETECTED] Incrementing auth check difficulty...");
        }
    }

    @Override
    public int getTickDelay() {
        return 5;
    }

    @Override
    public void tick() {
        count = 0;
    }
}

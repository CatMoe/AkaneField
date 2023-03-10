package catmoe.fallencrystal.akanefield.common.helper;

import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;
import catmoe.fallencrystal.akanefield.common.utils.Version;

public class PerformanceHelper {
    private static PerformanceMode performanceMode;
    private static ServerType serverType;

    public static void init(ServerType serverType) {
        int cores = Version.getCores();
        PerformanceHelper.serverType = serverType;
        if (!ConfigManager.detectServerPerformance) {
            performanceMode = PerformanceMode.CUSTOM;
            return;
        }
        if (cores <= 2) {
            performanceMode = PerformanceMode.LOW;
        }
        if (cores > 2 && cores <= 6 && serverType.equals(ServerType.VELOCITY)
                || serverType.equals(ServerType.BUNGEECORD)) {
            performanceMode = PerformanceMode.AVERAGE;
        }
        if (cores >= 6) {
            performanceMode = PerformanceMode.AVERAGE;
        }
        if (cores >= 10) {
            performanceMode = PerformanceMode.MAX;
        }
    }

    public static PerformanceMode getPerformanceMode() {
        return performanceMode;
    }

    public static boolean isEnabled() {
        return performanceMode == PerformanceMode.CUSTOM;
    }

    public static ServerType getRunning() {
        return serverType;
    }
}

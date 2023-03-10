package catmoe.fallencrystal.akanefield.common.thread;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.helper.PerformanceHelper;
import catmoe.fallencrystal.akanefield.common.helper.ServerType;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;

public class LatencyThread {
    private String result;
    private double count;

    public LatencyThread(IAntiBotPlugin iAntiBotPlugin) {
        iAntiBotPlugin.getLogHelper().debug("Enabled " + this.getClass().getSimpleName() + "!");
        this.result = "§cdisabled (see config)";
        this.count = 0;
        long nsv = 1250000000;
        if (PerformanceHelper.getRunning().equals(ServerType.SPIGOT)) {
            this.result = "§cdisabled (on bukkit)";
            return;
        }
        if (PerformanceHelper.isEnabled() && !PerformanceHelper.getPerformanceMode().isLatencyThreadEnabled()) {
            iAntiBotPlugin.getLogHelper().warn(
                    "Since you have the performance mode enabled, we found that the latency thread must be disabled, otherwise it could create stability problems (read config), to enable it, you must disable it in the config section 'detect-server-performance', and activate the 'enable-latency-thread'");
            this.result = "§c(disabled (detect-server-performance: true))";
            return;
        }
        if (ConfigManager.enableLatencyThread) {
            new Thread(() -> {
                while (iAntiBotPlugin.isRunning()) {
                    long first = System.nanoTime();
                    try {
                        Thread.sleep(1250L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.count = System.nanoTime() - first - nsv;
                    this.result = String.valueOf(count);
                }
            }, "AkaneField#LatencyService").start();
        }
    }

    public String getLatency() {
        return result + " ms";
    }

}

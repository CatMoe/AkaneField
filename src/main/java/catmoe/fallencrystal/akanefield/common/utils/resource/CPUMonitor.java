package catmoe.fallencrystal.akanefield.common.utils.resource;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import java.util.DoubleSummaryStatistics;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
public final class CPUMonitor {
    private int index = 0;
    public volatile static double recentProcessCpuLoadSnapshot = 0;
    private volatile double recentSystemCpuLoadSnapshot = 0;
    private final double[] recentProcessUsage = new double[20];

    private final ScheduledExecutorService executor;
    private final Future<?> monitorTask;

    public CPUMonitor() {
        final ScheduledThreadPoolExecutor ex = new ScheduledThreadPoolExecutor(1);
        ex.setRemoveOnCancelPolicy(true);
        this.executor = Executors.unconfigurableScheduledExecutorService(ex);
        this.monitorTask = this.executor.scheduleAtFixedRate(this::recordUsage, 0L, 50L, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        this.monitorTask.cancel(false);
        this.executor.shutdown();
    }

    private void nextIndex() {
        this.index++;
        if (this.index == 20) {
            this.index = 0;
        }
    }

    private void recordUsage() {
        this.recentProcessUsage[this.index] = currentProcessCpuLoad();
        this.recentProcessCpuLoadSnapshot = this.recentProcessCpuLoad();
        this.nextIndex();
    }

    public double recentProcessCpuLoadSnapshot() {
        return this.recentProcessCpuLoadSnapshot;
    }

    public double recentSystemCpuLoadSnapshot() {
        return this.recentSystemCpuLoadSnapshot;
    }

    private double recentProcessCpuLoad() {
        return round(average(this.recentProcessUsage));
    }

    private static double average(final double[] values) {
        final DoubleSummaryStatistics statistics = new DoubleSummaryStatistics();
        for (final double d : values.clone()) {
            if (d != 0 && !Double.isNaN(d)) {
                statistics.accept(d);
            }
        }
        return statistics.getAverage();
    }

    private static double round(final double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static double currentProcessCpuLoad() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad() * 100;
    }
}
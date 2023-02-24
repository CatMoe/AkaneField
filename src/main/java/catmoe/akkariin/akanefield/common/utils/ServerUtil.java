package catmoe.akkariin.akanefield.common.utils;

import catmoe.akkariin.akanefield.common.IServerPlatform;
import catmoe.akkariin.akanefield.common.FieldRunnable;

import java.io.File;

public class ServerUtil {
    private static IServerPlatform platform;
    public static long blacklistPercentage = 0;
    private static long lastAttack;

    public static void setPlatform(IServerPlatform platform) {
        ServerUtil.platform = platform;
    }

    public static long getLastAttack() {
        return lastAttack;
    }

    public static void setLastAttack(long lastAttack) {
        ServerUtil.lastAttack = lastAttack;
    }

    public static void cancelTask(FieldRunnable runnable) {
        platform.cancelTask(runnable.getTaskID());
    }

    public static File getDataFolder() {
        return platform.getDFolder();
    }

    public static void broadcast(String message) {
        platform.broadcast(message);
    }

    public static String colorize(String text) {
        return platform.colorize(text);
    }
}

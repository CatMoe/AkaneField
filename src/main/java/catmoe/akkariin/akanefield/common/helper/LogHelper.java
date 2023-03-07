package catmoe.akkariin.akanefield.common.helper;

import catmoe.akkariin.akanefield.common.IServerPlatform;
import catmoe.akkariin.akanefield.common.utils.ConfigManager;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.common.utils.ServerUtil;

import static catmoe.akkariin.akanefield.common.helper.LogHelper.LogType.*;

public class LogHelper {
    private final IServerPlatform platform;

    public LogHelper(IServerPlatform platform) {
        this.platform = platform;
    }

    public void debug(String msg) {
        if (!ConfigManager.isDebugModeOnline)
            return;
        platform.log(INFO, ServerUtil.colorize("&bDebug &7» " + msg));
    }

    public void warn(String msg) {
        platform.log(WARNING, ServerUtil.colorize(MessageManager.prefix + msg));
    }

    public void error(String msg) {
        platform.log(ERROR, ServerUtil.colorize(MessageManager.prefix + "&c" + msg));
    }

    public void info(String msg) {
        platform.log(INFO, ServerUtil.colorize(MessageManager.prefix + msg));
    }

    public void sendLogo() {
        info("§b ________      ___  __        ________      ________       _______           §f________  ___      _______       ___           ________     ");
        info("§b|\\   __  \\    |\\  \\|\\  \\     |\\   __  \\    |\\   ___  \\    |\\  ___ \\         §f|\\  _____\\|\\  \\    |\\  ___ \\     |\\  \\         |\\   ___ \\    ");
        info("§b\\ \\  \\|\\  \\   \\ \\  \\/  /|_   \\ \\  \\|\\  \\   \\ \\  \\\\ \\  \\   \\ \\   __/|        §f\\ \\  \\__/ \\ \\  \\   \\ \\   __/|    \\ \\  \\        \\ \\  \\_|\\ \\   ");
        info("§b \\ \\   __  \\   \\ \\   ___  \\   \\ \\   __  \\   \\ \\  \\\\ \\  \\   \\ \\  \\_|/__       §f\\ \\   __\\ \\ \\  \\   \\ \\  \\_|/__   \\ \\  \\        \\ \\  \\ \\\\ \\  ");
        info("§b  \\ \\  \\ \\  \\   \\ \\  \\\\ \\  \\   \\ \\  \\ \\  \\   \\ \\  \\\\ \\  \\   \\ \\  \\_|\\ \\       §f\\ \\  \\_|  \\ \\  \\   \\ \\  \\_|\\ \\   \\ \\  \\____    \\ \\  \\_\\\\ \\ ");
        info("§b   \\ \\__\\ \\__\\   \\ \\__\\\\ \\__\\   \\ \\__\\ \\__\\   \\ \\__\\\\ \\__\\   \\ \\_______\\       §f\\ \\__\\    \\ \\__\\   \\ \\_______\\   \\ \\_______\\   \\ \\_______\\");
        info("§b    \\|__|\\|__|    \\|__| \\|__|    \\|__|\\|__|    \\|__| \\|__|    \\|_______|        §f\\|__|     \\|__|    \\|_______|    \\|_______|    \\|_______|");
        info("");
        info("");
    }

    public enum LogType {
        INFO, WARNING, ERROR
    }
}

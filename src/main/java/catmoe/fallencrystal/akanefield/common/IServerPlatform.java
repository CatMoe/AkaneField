package catmoe.fallencrystal.akanefield.common;

import java.io.File;

import catmoe.fallencrystal.akanefield.common.helper.LogHelper;

public interface IServerPlatform {
    String colorize(String text);

    void cancelTask(int id);

    void log(LogHelper.LogType type, String log);

    void broadcast(String message);

    File getDFolder();
}

package catmoe.akkariin.akanefield.common;

import catmoe.akkariin.akanefield.common.helper.LogHelper;

import java.io.File;

public interface IServerPlatform {
    String colorize(String text);

    void cancelTask(int id);

    void log(LogHelper.LogType type, String log);

    void broadcast(String message);

    File getDFolder();
}

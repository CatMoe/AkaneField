package catmoe.akkariin.akanefield.common.objects.filter;

import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;

import java.util.List;
import java.util.logging.LogRecord;

public class Proxy247Filter {
    private final IAntiBotManager antiBotManager;
    private final List<String> blocked;

    public Proxy247Filter(IAntiBotPlugin antiBotPlugin) {
        this.antiBotManager = antiBotPlugin.getAntiBotManager();
        this.blocked = antiBotPlugin.getConfigYml().getStringList("persistent-filter");
    }

    public boolean isLoggable(LogRecord record) {
        if (antiBotManager.isSomeModeOnline())
            return true;
        String message = record.getMessage().toLowerCase();
        for (String s : blocked) {
            if (message.contains(s.toLowerCase()))
                return false;
        }
        return true;
    }
}

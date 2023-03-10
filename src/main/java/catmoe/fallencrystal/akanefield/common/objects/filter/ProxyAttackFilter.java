package catmoe.fallencrystal.akanefield.common.objects.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;

public class ProxyAttackFilter implements Filter {
    private final IAntiBotManager antiBotManager;
    private final List<String> blocked;

    private final Proxy247Filter proxy247Filter;

    public ProxyAttackFilter(IAntiBotPlugin antiBotPlugin) {
        this.antiBotManager = antiBotPlugin.getAntiBotManager();
        this.proxy247Filter = new Proxy247Filter(antiBotPlugin);
        this.blocked = new ArrayList<>(Arrays.asList(
                "InitialHandler has",
                "Connection reset by peer",
                "Unexpected packet received",
                "read timed out",
                "could not decode packet",
                "to process",
                "Empty Packet!",
                "corrupted",
                "has pinged",
                "has connected",
                "in packet",
                "bad packet ID",
                "bad packet",
                "encountered exception",
                "com.mojang.authlib",
                "lost connection: Timed out",
                "lost connection: Disconnected",
                "Took too long to log in",
                "disconnected with",
                "read time out",
                "Connect reset by peer",
                "overflow in packet",
                "pipeline",
                "The received encoded string",
                "is longer than maximum allowed"));
        blocked.addAll(antiBotPlugin.getConfigYml().getStringList("attack-filter"));
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        if (antiBotManager.isSomeModeOnline()) {
            if (antiBotManager.isPacketModeEnabled())
                antiBotManager.increasePacketPerSecond();
            return !record.getMessage().toLowerCase().contains("{0}");
        }

        if (isDenied(record.getMessage())) {
            antiBotManager.increasePacketPerSecond();
            if (antiBotManager.getPacketPerSecond() > ConfigManager.packetModeTrigger)
                antiBotManager.enablePacketMode();
        }

        return proxy247Filter.isLoggable(record);
    }

    public boolean isDenied(String record) {
        for (String str : blocked) {
            if (record.contains(str)) {
                return true;
            }
        }
        return false;
    }
}

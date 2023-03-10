package catmoe.fallencrystal.akanefield.common.checks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;

public class NameChangerCheck implements JoinCheck {
    private final IAntiBotPlugin plugin;
    private final Map<String, Set<String>> data;

    public NameChangerCheck(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.data = new ConcurrentHashMap<>();
        loadTask();
        if (isEnabled()) {
            plugin.getLogHelper().debug("Loaded " + this.getClass().getSimpleName() + "!");
        }
    }

    /**
     * @param ip   ip of the player
     * @param name name of the player
     * @return if player need to be disconnected from the check
     */
    public boolean isDenied(String ip, String name) {
        if (!isEnabled()) {
            return false;
        }
        if (!plugin.getAntiBotManager().isSomeModeOnline())
            return false;

        Set<String> nicks = data.computeIfAbsent(ip, k -> new CopyOnWriteArraySet<>());
        nicks.add(name);
        boolean res = nicks.size() >= ConfigManager.nameChangerLimit;

        if (res) {
            data.remove(ip);
        }
        return res;
    }

    @Override
    public void onDisconnect(String ip, String name) {

    }

    @Override
    public boolean isEnabled() {
        return ConfigManager.isNameChangerEnabled;
    }

    @Override
    public void loadTask() {
        plugin.scheduleRepeatingTask(data::clear, false, 1000L * ConfigManager.nameChangerTime);
    }

}

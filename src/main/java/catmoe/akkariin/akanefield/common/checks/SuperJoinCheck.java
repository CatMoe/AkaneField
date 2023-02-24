package catmoe.akkariin.akanefield.common.checks;

import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.objects.FancyInteger;
import catmoe.akkariin.akanefield.common.utils.ConfigManger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SuperJoinCheck implements JoinCheck {

    private final IAntiBotPlugin plugin;
    private final Map<String, FancyInteger> data;

    public SuperJoinCheck(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        plugin.getAntiBotManager().getBlackListService();
        this.data = new ConcurrentHashMap<>();
        loadTask();
        if (isEnabled()) {
            plugin.getLogHelper().debug("Loaded " + this.getClass().getSimpleName() + "!");
        }
    }

    @Override
    public boolean isDenied(String ip, String name) {
        if (!isEnabled()) {
            return false;
        }
        FancyInteger i = data.getOrDefault(ip, new FancyInteger(0));
        i.increase();
        data.put(ip, i);

        if (i.get() > ConfigManger.superJoinLimit) {
            data.remove(ip);
            plugin.getLogHelper().debug("[UAB DEBUG] Detected attack on SuperJoinCheck!");
            return true;
        }

        return false;
    }

    @Override
    public void onDisconnect(String ip, String name) {

    }

    @Override
    public boolean isEnabled() {
        return ConfigManger.isSuperJoinEnabled;
    }

    @Override
    public void loadTask() {
        plugin.scheduleRepeatingTask(data::clear, false, 1000L * ConfigManger.superJoinTime);
    }
}

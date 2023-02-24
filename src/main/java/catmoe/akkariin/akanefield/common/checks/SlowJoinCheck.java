package catmoe.akkariin.akanefield.common.checks;

import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import java.util.ArrayList;
import java.util.List;

public class SlowJoinCheck implements JoinCheck {
    private final IAntiBotPlugin plugin;
    private final List<String> lastNames;

    public SlowJoinCheck(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.lastNames = new ArrayList<>();
    }

    @Override
    public boolean isDenied(String ip, String name) {
        return false;
    }

    @Override
    public void onDisconnect(String ip, String name) {
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void loadTask() {
        plugin.scheduleRepeatingTask(lastNames::clear, false, 1000L * 60L);
    }
}

package catmoe.akkariin.akanefield.common.checks;

import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.objects.profile.BlackListReason;
import catmoe.akkariin.akanefield.common.utils.ConfigManger;

import java.util.List;

public class InvalidNameCheck implements JoinCheck {
    private final IAntiBotPlugin plugin;
    private final IAntiBotManager antiBotManager;
    private final List<String> invalidNames;

    public InvalidNameCheck(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.antiBotManager = plugin.getAntiBotManager();
        this.invalidNames = ConfigManger.invalidNamesBlockedEntries;
    }

    @Override
    public boolean isDenied(String ip, String name) {
        for (String blacklisted : invalidNames) {
            blacklisted = blacklisted.toLowerCase();

            if (name.toLowerCase().contains(blacklisted)) {
                antiBotManager.getBlackListService().blacklist(ip, BlackListReason.STRANGE_PLAYER, name);
                plugin.getLogHelper().debug("[UAB DEBUG] Detected attack on InvalidNameCheck!");
                return true;
            }
        }

        return false;
    }

    @Override
    public void onDisconnect(String ip, String name) {

    }

    @Override
    public boolean isEnabled() {
        return ConfigManger.isInvalidNameCheckEnabled;
    }

    @Override
    public void loadTask() {

    }
}

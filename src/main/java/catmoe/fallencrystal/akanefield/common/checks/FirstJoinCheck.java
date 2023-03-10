package catmoe.fallencrystal.akanefield.common.checks;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.service.UserDataService;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;

public class FirstJoinCheck implements JoinCheck {
    private final UserDataService userDataService;

    public FirstJoinCheck(IAntiBotPlugin plugin) {
        this.userDataService = plugin.getUserDataService();
        if (isEnabled()) {
            plugin.getLogHelper().debug("Loaded " + this.getClass().getSimpleName() + "!");
        }
    }

    /**
     * @param ip   - ip of the player
     * @param name - name of the player
     * @return - false if player can join or true if is first join
     */
    public boolean isDenied(String ip, String name) {
        if (!isEnabled()) {
            return false;
        }
        return userDataService.isFirstJoin(ip);
    }

    @Override
    public void onDisconnect(String ip, String name) {

    }

    public boolean isEnabled() {
        return ConfigManager.isFirstJoinEnabled;
    }

    @Override
    public void loadTask() {

    }
}

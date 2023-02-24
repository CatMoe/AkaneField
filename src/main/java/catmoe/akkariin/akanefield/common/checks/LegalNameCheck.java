package catmoe.akkariin.akanefield.common.checks;

import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.objects.profile.BlackListReason;
import catmoe.akkariin.akanefield.common.utils.ConfigManger;

public class LegalNameCheck implements JoinCheck {
    private static String VALID_NAME_REGEX = "[a-zA-Z0-9_.]*";

    private final IAntiBotPlugin plugin;
    private final IAntiBotManager antiBotManager;

    public LegalNameCheck(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.antiBotManager = plugin.getAntiBotManager();

        if (ConfigManger.legalNameCheckRegex != null) {
            VALID_NAME_REGEX = ConfigManger.legalNameCheckRegex;
        }
    }

    @Override
    public boolean isDenied(String ip, String name) {
        if (!isEnabled())
            return false;
        if (name.matches(VALID_NAME_REGEX) && name.length() <= 16) {
            return false;
        }

        antiBotManager.getBlackListService().blacklist(ip, BlackListReason.STRANGE_PLAYER, "_INVALID_");
        plugin.getLogHelper().debug("[UAB DEBUG] Detected bot on LegalNameCheck!");
        return true;
    }

    @Override
    public void onDisconnect(String ip, String name) {

    }

    @Override
    public boolean isEnabled() {
        return ConfigManger.isLegalNameCheckEnabled;
    }

    @Override
    public void loadTask() {

    }
}

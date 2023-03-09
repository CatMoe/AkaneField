package catmoe.akkariin.akanefield.common.checks;

import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.objects.profile.BlackListReason;
import catmoe.akkariin.akanefield.common.utils.ConfigManager;
import catmoe.akkariin.akanefield.common.utils.MessageManager;

public class LegalNameCheck implements JoinCheck {
    private static String VALID_NAME_REGEX = "[a-zA-Z0-9_.]*";

    private final IAntiBotPlugin plugin;
    private final IAntiBotManager antiBotManager;

    public LegalNameCheck(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.antiBotManager = plugin.getAntiBotManager();

        if (ConfigManager.legalNameCheckRegex != null) {
            VALID_NAME_REGEX = ConfigManager.legalNameCheckRegex;
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
        plugin.getLogHelper()
                .debug(MessageManager.getMessage("debug.prefix" + MessageManager.getMessage("debug.checks.message"))
                        .replace("%type%", MessageManager.getMessage("debug.checks.type.legalname")));
        return true;
    }

    @Override
    public void onDisconnect(String ip, String name) {

    }

    @Override
    public boolean isEnabled() {
        return ConfigManager.isLegalNameCheckEnabled;
    }

    @Override
    public void loadTask() {

    }
}

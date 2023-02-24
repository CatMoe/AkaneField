package catmoe.akkariin.akanefield.common.core;

import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.service.BlackListService;
import catmoe.akkariin.akanefield.common.service.DetectorService;
import catmoe.akkariin.akanefield.common.service.WhitelistService;
import catmoe.akkariin.akanefield.common.utils.ConfigManger;
import catmoe.akkariin.akanefield.common.utils.ServerUtil;

import java.util.concurrent.TimeUnit;

public class AkaneFieldCore {
    private final IAntiBotPlugin plugin;
    private final BlackListService blackListService;
    private final WhitelistService whitelistService;

    public AkaneFieldCore(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.blackListService = plugin.getAntiBotManager().getBlackListService();
        this.whitelistService = plugin.getAntiBotManager().getWhitelistService();
    }

    public void load() {

        plugin.scheduleRepeatingTask(() -> {
            refresh();
            DetectorService.tickDetectors();
        }, false, 1000L);

        plugin.scheduleRepeatingTask(plugin.getAntiBotManager().getQueueService()::clear, false,
                ConfigManger.taskManagerClearCache * 1000L);
        plugin.scheduleDelayedTask(this::checkAutoPurger, true, 300000L); // 5 min
        plugin.scheduleRepeatingTask(() -> {
            if (plugin.getAntiBotManager().isAntiBotModeEnabled()) {
                return;
            }
            for (String blacklisted : blackListService.getBlackListedIPS()) {
                whitelistService.unWhitelist(blacklisted);
            }
        }, false, 1000L * ConfigManger.taskManagerClearCache);
    }

    public void refresh() {
        if (ConfigManger.isConsoleAttackMessageDisabled) {
            return;
        }
        plugin.getAntiBotManager().dispatchConsoleAttackMessage();
    }

    private void checkAutoPurger() {
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTimeMillis = currentTimeMillis - ServerUtil.getLastAttack();
        long elapsedTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis);
        // se il server è sotto attacco o l'ultimo attacco è stato meno di 10 minuti fà
        // ritorna
        if (elapsedTimeMinutes < 10 || plugin.getAntiBotManager().isSomeModeOnline())
            return;

        // check for blacklist autopurge
        String blacklistType = ConfigManger.getAutoPurgerParam("blacklist.type");
        boolean blacklistEnabled = ConfigManger.getAutoPurgerBoolean("blacklist.enabled");

        if (blacklistType != null && blacklistEnabled) {
            switch (blacklistType) {
                case "LIMIT":
                    int required = ConfigManger.getAutoPurgerValue("blacklist.value");
                    if (plugin.getAntiBotManager().getBlackListService().size() >= required) {
                        plugin.getAntiBotManager().getBlackListService().clear();
                    }
                    break;
                case "AFTER_ATTACK":
                    plugin.getAntiBotManager().getBlackListService().clear();
                    break;
            }
        }

        // check for whitelist autopurge
        String whitelistType = ConfigManger.getAutoPurgerParam("whitelist.type");
        boolean whitelistEnabled = ConfigManger.getAutoPurgerBoolean("whitelist.enabled");

        if (whitelistType != null && whitelistEnabled) {
            switch (whitelistType) {
                case "LIMIT":
                    int required = ConfigManger.getAutoPurgerValue("whitelist.value");
                    if (plugin.getAntiBotManager().getWhitelistService().size() >= required) {
                        plugin.getAntiBotManager().getWhitelistService().clear();
                    }
                    break;
                case "AFTER_ATTACK":
                    plugin.getAntiBotManager().getWhitelistService().clear();
                    break;
            }
        }
    }
}

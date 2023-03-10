package catmoe.fallencrystal.akanefield.listener;

import catmoe.fallencrystal.akanefield.Notificator;
import catmoe.fallencrystal.akanefield.common.AttackState;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.ModeType;
import catmoe.fallencrystal.akanefield.common.detectors.FastJoinBypassDetector;
import catmoe.fallencrystal.akanefield.common.service.AttackTrackerService;
import catmoe.fallencrystal.akanefield.common.utils.ConfigManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.event.AttackStateEvent;
import catmoe.fallencrystal.akanefield.event.DuringAttackIPJoinEvent;
import catmoe.fallencrystal.akanefield.event.ModeEnableEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CustomEventListener implements Listener {

    private final IAntiBotPlugin plugin;
    private final FastJoinBypassDetector bypassDetector;

    private final AttackTrackerService trackerService;

    public CustomEventListener(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        this.bypassDetector = new FastJoinBypassDetector(plugin);
        this.trackerService = plugin.getAttackTrackerService();
    }

    @EventHandler
    public void onAttack(ModeEnableEvent e) {
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.hasPermission("akanefield.notification.automatic")) {
                Notificator.automaticNotification(player);
            }
        }

        if (e.getEnabledMode().equals(ModeType.FastJoin) || e.getEnabledMode().equals(ModeType.SlowJoin)) {
            if (ConfigManager.antibotDisconnect) {
                e.disconnectBots();
            }
        }
    }

    @EventHandler
    public void onAttackStop(AttackStateEvent e) {
        if (e.getAttackState() != AttackState.STOPPED) {
            return;
        }

        plugin.scheduleDelayedTask(() -> {
            if (plugin.getAntiBotManager().isSomeModeOnline())
                return;
            if (ConfigManager.disableNotificationAfterAttack) {
                Notificator.disableAllNotifications();
            }

            trackerService.onAttackStop();
            ServerUtil.setLastAttack(System.currentTimeMillis());

            plugin.scheduleDelayedTask(() -> {
                if (plugin.getAntiBotManager().isSomeModeOnline())
                    return;
                plugin.getAntiBotManager().getBlackListService().save();
                plugin.getUserDataService().unload();
                plugin.getWhitelist().save();
            }, true, 1000L);
        }, false, 1000L * 3);
    }

    @EventHandler
    public void onAttackStart(ModeEnableEvent e) {
        if (e.getEnabledMode() == ModeType.Block)
            return;

        trackerService.onNewAttackStart();
    }

    @EventHandler
    public void onIPJoinDuringAttack(DuringAttackIPJoinEvent e) {
        bypassDetector.registerJoin();
    }
}

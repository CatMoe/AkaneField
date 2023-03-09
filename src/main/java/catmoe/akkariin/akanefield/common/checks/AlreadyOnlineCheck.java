package catmoe.akkariin.akanefield.common.checks;

import catmoe.akkariin.akanefield.common.utils.ConfigManager;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.utils.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.BungeeCord;

public class AlreadyOnlineCheck implements Listener {

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        if (ConfigManager.getAlreadyOnlineKickEnabled() == true) {
            if (event.isCancelled()) {
                return;
            }
            for (ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) {
                if (!player.getName().equals(event.getConnection().getName()))
                    continue;
                event.setCancelReason(ComponentBuilder.buildColorized(MessageManager.getAlreadyOnlineMessage()));
                event.setCancelled(true);
            }
        }
    }
}

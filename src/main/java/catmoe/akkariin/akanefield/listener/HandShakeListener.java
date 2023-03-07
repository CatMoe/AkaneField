package catmoe.akkariin.akanefield.listener;

import catmoe.akkariin.akanefield.common.objects.profile.BlackListReason;
import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.utils.ConfigManager;
import catmoe.akkariin.akanefield.utils.Utils;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.Handshake;

public class HandShakeListener implements Listener {

    private final IAntiBotManager antiBotManager;

    public HandShakeListener(IAntiBotPlugin plugin) {
        this.antiBotManager = plugin.getAntiBotManager();
    }

    @EventHandler
    public void onHandShake(PlayerHandshakeEvent e) {
        Handshake handshake = e.getHandshake();
        String ip = Utils.getIP(e.getConnection());

        if (handshake.getRequestedProtocol() > 2 && antiBotManager.isSomeModeOnline()) {
            handshake.setRequestedProtocol(2); // converting to join
            if (ConfigManager.blacklistInvalidProtocol) {
                antiBotManager.getBlackListService().blacklist(ip, BlackListReason.STRANGE_PLAYER);
            }
        }
    }
}

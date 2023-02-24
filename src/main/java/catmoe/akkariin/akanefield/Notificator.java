package catmoe.akkariin.akanefield;

import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.INotificator;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.common.utils.ServerUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class Notificator implements INotificator {

    private static final List<ProxiedPlayer> actionbars = new ArrayList<>();

    public static void automaticNotification(ProxiedPlayer player) {
        if (actionbars.contains(player))
            return;
        actionbars.remove(player);
        actionbars.add(player);
    }

    public static void toggleActionBar(ProxiedPlayer player) {
        if (actionbars.contains(player)) {
            actionbars.remove(player);
        } else {
            actionbars.add(player);
        }
        player.sendMessage(
                new TextComponent(ServerUtil.colorize(MessageManager.prefix + MessageManager.toggledActionbar)));
    }

    public static void disableAllNotifications() {
        actionbars.clear();
    }

    public void sendActionbar(String str) {
        actionbars
                .forEach(ac -> ac.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ServerUtil.colorize(str))));
    }

    public void init(IAntiBotPlugin plugin) {
        plugin.scheduleRepeatingTask(() -> {
            if (plugin.getAntiBotManager().isPacketModeEnabled()) {
                sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarPackets));
                return;
            }
            if (plugin.getAntiBotManager().isSomeModeOnline()) {
                sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarAntiBotMode));
            } else {
                sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarOffline));
            }
        }, false, 125L);
    }
}

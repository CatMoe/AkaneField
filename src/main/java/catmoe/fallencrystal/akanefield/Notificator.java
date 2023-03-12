package catmoe.fallencrystal.akanefield;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.INotificator;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.utils.Utils;

public class Notificator implements INotificator {

    private static final List<ProxiedPlayer> actionbars = new ArrayList<>();
    private static final List<ProxiedPlayer> titles = new ArrayList<>();
    private static final List<ProxiedPlayer> broadcast = new ArrayList<>();

    public static void automaticNotification(ProxiedPlayer player) {
        if (MessageManager.attackNotificationTitleEnabled == true) {
            if (titles.contains(player)) {
                titles.remove(player);
            } else {
                sendTitle(player, MessageManager.attackNotificationTitleTitle,
                        MessageManager.attackNotificationTitlesubTitle,
                        MessageManager.attackNotificationTitleStay, MessageManager.attackNotificationTitleFadeIn,
                        MessageManager.attackNotificationTitleFadeOut);
            }
        }
        if (MessageManager.attackNotificationChatEnabled == true) {
            if (broadcast.contains(player)) {
                broadcast.remove(player);
            } else {
                broadcast.add(player);
                broadcast.forEach(ac -> ac.sendMessage(ChatMessageType.CHAT, new TextComponent(
                        Utils.colora(MessageManager.prefix + MessageManager.attackNotificationChatMessages))));
            }
        }
        actionbars.remove(player);
        actionbars.add(player);
    }

    public static void sendTitle(ProxiedPlayer player, String title, String subTitle, int stay, int fadeIn,
            int fadeOut) {
        titles.add(player);
        Title t = ProxyServer.getInstance().createTitle();
        t.title(new TextComponent(ServerUtil.colorize(title)));
        t.subTitle(new TextComponent(ServerUtil.colorize(subTitle)));
        t.stay(stay);
        t.fadeIn(fadeIn);
        t.fadeOut(fadeOut);
        titles.forEach(t::send);
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
                .forEach(ac -> ac.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Utils.colora(str))));
    }

    public void init(IAntiBotPlugin plugin) {
        // actionbar
        plugin.scheduleRepeatingTask(() -> {
            if (plugin.getAntiBotManager().isPacketModeEnabled()) {
                sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarFirewall));
                return;
            }
            if (plugin.getAntiBotManager().isAntiBotModeEnabled()) {
                if (plugin.getAntiBotManager().isPingModeEnabled()) {
                    sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarCombined));
                } else {
                    if (plugin.getAntiBotManager().isAntiBotModeEnabled()) {
                        sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarBlock));
                    }
                }
            }
            if (plugin.getAntiBotManager().isSlowAntiBotModeEnabled()) {
                if (plugin.getAntiBotManager().isPingModeEnabled()) {
                    sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarCombined));
                } else {
                    sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarVerify));
                }
            }
            if (plugin.getAntiBotManager().isPingModeEnabled()) {
                if (plugin.getAntiBotManager().isSlowAntiBotModeEnabled()) {
                }
                if (plugin.getAntiBotManager().isAntiBotModeEnabled()) {
                } else {
                    sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarMotd));
                }
            }
            if (plugin.getAntiBotManager().isSomeModeOnline()) {
                if (MessageManager.attackNotificationTitleEnabled == true) {
                }
            } else {
                sendActionbar(plugin.getAntiBotManager().replaceInfo(MessageManager.actionbarIdle));
            }
        }, false, 50L);
    }
}

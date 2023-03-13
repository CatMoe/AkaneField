package catmoe.fallencrystal.akanefield.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MessageSendUtil {
    public static void actionbar(ProxiedPlayer p, String message) {
        List<ProxiedPlayer> actionbarp = new ArrayList<>();
        actionbarp.add(p);
        p.sendMessage(
                ChatMessageType.ACTION_BAR,
                new TextComponent(
                        ServerUtil.colorize(message)));
        actionbarp.remove(p);
    }

    public static void rawchat(ProxiedPlayer p, String message) {
        List<ProxiedPlayer> chatp = new ArrayList<>();
        chatp.add(p);
        p.sendMessage(
                ChatMessageType.CHAT,
                new TextComponent(
                        ServerUtil.colorize(message)));
        chatp.remove(p);
    }

    public static void prefixchat(ProxiedPlayer p, String message) {
        List<ProxiedPlayer> chatp = new ArrayList<>();
        chatp.add(p);
        p.sendMessage(
                ChatMessageType.CHAT,
                new TextComponent(
                        ServerUtil.colorize(
                                MessageManager.prefix + message)));
        chatp.remove(p);
    }

    public static void fulltitle(ProxiedPlayer p, String title, String subtitle, int stay, int fadeIn, int fadeOut) {
        List<ProxiedPlayer> titlep = new ArrayList<>();
        Title t = ProxyServer.getInstance().createTitle();
        titlep.add(p);
        t.title(
                new TextComponent(
                        ServerUtil.colorize(title)));
        t.subTitle(
                new TextComponent(
                        ServerUtil.colorize(subtitle)));
        t.stay(stay);
        t.fadeIn(fadeIn);
        t.fadeOut(fadeOut);
    }

    public static interface Platform {
        String colorize(String text);

        void cancelTask(int id);

        static void log(MessageSendUtil.LogType type, String log) {
        }

        void broadcast(String message);

        File getDFolder();
    }

    public static enum LogType {
        INFO, WARNING, ERROR
    }

    public static void loginfo(String message) {
        Platform.log(LogType.INFO, ServerUtil.colorize(MessageManager.prefix + message));
    }

    public static void logwarn(String message) {
        Platform.log(LogType.WARNING, ServerUtil.colorize(MessageManager.prefix + message));
    }

    public static void logerror(String message) {
        Platform.log(LogType.ERROR, ServerUtil.colorize(MessageManager.prefix + message));
    }
}

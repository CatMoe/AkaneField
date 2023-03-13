package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.AkaneFieldProxy;
import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;

public class TestMessageCommand implements SubCommand {
    private static final List<ProxiedPlayer> actionbars = new ArrayList<>();
    private static final List<ProxiedPlayer> titles = new ArrayList<>();

    public TestMessageCommand(AkaneFieldProxy akaneFieldProxy) {
    }

    @Override
    public String getSubCommandId() {
        return "text";
    }

    @Override
    public String getPermission() {
        return "akanefield.text";
    }

    @Override
    public int argsSize() {
        return 3;
    }

    public String getEmptyMessage() {
        return "";
    }

    @Override
    public Map<Integer, List<String>> getTabCompleter() {
        Map<Integer, List<String>> map = new HashMap<>();
        map.put(1, Arrays.asList("chat", "actionbar"));
        return map;
    }

    @Override
    public boolean allowedConsole() {
        return true;
    }

    public void sendActionbar(String str) {
        actionbars
                .forEach(ac -> ac.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ServerUtil.colorize(str))));
    }

    public void sendTestTitle(String title, String subTitle) {
        Title t = ProxyServer.getInstance().createTitle();
        t.title(new TextComponent(ServerUtil.colorize(title)));
        t.subTitle(new TextComponent(ServerUtil.colorize(subTitle)));
        t.stay(20);
        t.fadeIn(0);
        t.fadeOut(0);
        titles.forEach(t::send);
    }

    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        try {
            if (args[1].equalsIgnoreCase("chat")) {
                sender.sendMessage(ServerUtil.colorize(args[2]));
                return;
            }
            if (args[1].equalsIgnoreCase("title")) {
                titles.add((ProxiedPlayer) sender);
                sendTestTitle(args[2], getEmptyMessage());
                titles.remove(sender);
                return;
            }
            if (args[1].equalsIgnoreCase("subtitle")) {
                titles.add((ProxiedPlayer) sender);
                sendTestTitle(getEmptyMessage(), args[2]);
                titles.remove(sender);
                return;
            }
            if (args[1].equalsIgnoreCase("actionbar")) {
                actionbars.add((ProxiedPlayer) sender);
                sendActionbar(ServerUtil.colorize(args[2]));
                actionbars.remove(sender);
                return;
            } else {
                sender.sendMessage(
                        ServerUtil.colorize(
                                MessageManager.prefix + "&b/af text &f[chat|actionbar|title|subtitle] &b<text>"));
                return;
            }
        } catch (Exception e) {
            sender.sendMessage(ServerUtil.colorize(MessageManager.prefix + "&cCannot Parse."));
            return;
        }
    }
}

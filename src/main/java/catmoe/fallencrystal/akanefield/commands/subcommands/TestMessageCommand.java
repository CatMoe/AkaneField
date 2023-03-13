package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.AkaneFieldProxy;
import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.utils.MessageSendUtil;

public class TestMessageCommand implements SubCommand {
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

    public void execute(CommandSender sender, String[] args) {
        try {
            if (args[1].equalsIgnoreCase("chat")) {
                MessageSendUtil.rawchat((ProxiedPlayer) sender, args[2]);
                return;
            }
            if (args[1].equalsIgnoreCase("title")) {
                MessageSendUtil.fulltitle((ProxiedPlayer) sender, args[2], "", 20, 0, 0);
                return;
            }
            if (args[1].equalsIgnoreCase("subtitle")) {
                MessageSendUtil.fulltitle((ProxiedPlayer) sender, "", args[2], 20, 0, 0);
                return;
            }
            if (args[1].equalsIgnoreCase("actionbar")) {
                MessageSendUtil.actionbar((ProxiedPlayer) sender, args[2]);
                return;
            } else {
                MessageSendUtil.prefixchat((ProxiedPlayer) sender, "&factionbar &b| &fchat &b| &ftitle &b| &fsubtitle");
                return;
            }
        } catch (Exception e) {
            MessageSendUtil.prefixchat((ProxiedPlayer) sender, "&cCannot Parse.");
            return;
        }
    }
}

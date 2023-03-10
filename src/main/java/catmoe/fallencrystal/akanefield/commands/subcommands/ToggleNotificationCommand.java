package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.Notificator;
import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.utils.Utils;

public class ToggleNotificationCommand implements SubCommand {

    @Override
    public String getSubCommandId() {
        return "toggle";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (args[1].equals("actionbar")) {
            Notificator.toggleActionBar((ProxiedPlayer) sender);
        } else {
            sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.commandWrongArgument));
        }
    }

    @Override
    public String getPermission() {
        return "akanefield.toggle";
    }

    @Override
    public int argsSize() {
        return 2;
    }

    @Override
    public Map<Integer, List<String>> getTabCompleter() {
        Map<Integer, List<String>> map = new HashMap<>();
        map.put(1, Arrays.asList("actionbar"));
        return map;
    }

    @Override
    public boolean allowedConsole() {
        return false;
    }
}
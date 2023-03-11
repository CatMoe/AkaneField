package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.Notificator;
import catmoe.fallencrystal.akanefield.commands.SubCommand;

public class ToggleNotificationCommand implements SubCommand {

    @Override
    public String getSubCommandId() {
        return "toggle";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Notificator.toggleActionBar((ProxiedPlayer) sender);
        return;
    }

    @Override
    public String getPermission() {
        return "akanefield.notification";
    }

    @Override
    public int argsSize() {
        return 1;
    }

    @Override
    public Map<Integer, List<String>> getTabCompleter() {
        return null;
    }

    @Override
    public boolean allowedConsole() {
        return false;
    }
}

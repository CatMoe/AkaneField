package catmoe.fallencrystal.akanefield.commands.subcommands;

import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.gui.NullMenu;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TestMenuCommand implements SubCommand {

    @Override
    public String getSubCommandId() {
        return "gui";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        NullMenu menu = new NullMenu();
        menu.define((ProxiedPlayer) sender);
        menu.open((ProxiedPlayer) sender);
        return;
    }

    @Override
    public String getPermission() {
        return "akanefield.gui";
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

package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.utils.Utils;

public class HelpCommand implements SubCommand {

    public HelpCommand(IAntiBotPlugin iAntiBotPlugin) {
    }

    @Override
    public String getSubCommandId() {
        return "help";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        MessageManager.helpMessage.forEach(a -> sender.sendMessage(Utils.colora(a)));
    }

    @Override
    public String getPermission() {
        return "akanefield.help";
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
        return true;
    }
}

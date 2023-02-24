package catmoe.akkariin.akanefield.commands.subcommands;

import catmoe.akkariin.akanefield.commands.SubCommand;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.utils.Utils;
import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.Map;

public class ReloadCommand implements SubCommand {
    private final IAntiBotPlugin plugin;

    public ReloadCommand(IAntiBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getSubCommandId() {
        return "reload";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender commandSender, String[] strings) {
        plugin.reload();
        commandSender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.reloadMessage));
    }

    @Override
    public String getPermission() {
        return "akanefield.reload";
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

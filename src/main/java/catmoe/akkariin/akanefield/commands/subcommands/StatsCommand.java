package catmoe.akkariin.akanefield.commands.subcommands;

import catmoe.akkariin.akanefield.commands.SubCommand;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.utils.Utils;
import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.Map;

public class StatsCommand implements SubCommand {

    private final IAntiBotPlugin iAntiBotPlugin;

    public StatsCommand(IAntiBotPlugin iAntiBotPlugin) {
        this.iAntiBotPlugin = iAntiBotPlugin;
    }

    @Override
    public String getSubCommandId() {
        return "stats";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        MessageManager.statsMessage
                .forEach(a -> sender.sendMessage(Utils.colora(iAntiBotPlugin.getAntiBotManager().replaceInfo(a))));
    }

    @Override
    public String getPermission() {
        return "akanefield.stats";
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

package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;

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
                .forEach(a -> sender
                        .sendMessage(ServerUtil.colorize(iAntiBotPlugin.getAntiBotManager().replaceInfo(a))));
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

package catmoe.akkariin.akanefield.commands.subcommands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catmoe.akkariin.akanefield.commands.SubCommand;
import catmoe.akkariin.akanefield.common.IAntiBotManager;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.utils.Utils;
import net.md_5.bungee.api.CommandSender;

@SuppressWarnings("deprecation")
public class ClearCommand implements SubCommand {

    private final IAntiBotManager antiBotManager;

    public ClearCommand(IAntiBotPlugin iAntiBotPlugin) {
        antiBotManager = iAntiBotPlugin.getAntiBotManager();
    }

    @Override
    public String getSubCommandId() {
        return "clear";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args[1].equals("whitelist")) {
            antiBotManager.getWhitelistService().clear();
            sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getCommandCleared("whiteList")));
        } else {
            if (args[1].equals("blacklist")) {
                antiBotManager.getBlackListService().clear();
                sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getCommandCleared("blackList")));
            } else {
                sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.commandWrongArgument));
            }
        }
    }

    @Override
    public String getPermission() {
        return "akanefield.clear";
    }

    @Override
    public int argsSize() {
        return 2;
    }

    @Override
    public Map<Integer, List<String>> getTabCompleter() {
        Map<Integer, List<String>> map = new HashMap<>();
        map.put(1, Arrays.asList("whitelist", "blacklist"));
        return map;
    }

    @Override
    public boolean allowedConsole() {
        return true;
    }
}

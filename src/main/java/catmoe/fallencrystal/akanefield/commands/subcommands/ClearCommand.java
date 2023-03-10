package catmoe.fallencrystal.akanefield.commands.subcommands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.utils.Utils;
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
            sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager
                    .getBlackWhiteListCleared(MessageManager.getMessage("white-black-list.type.whitelist"))));
        } else {
            if (args[1].equals("blacklist")) {
                antiBotManager.getBlackListService().clear();
                sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager
                        .getBlackWhiteListCleared(MessageManager.getMessage("white-black-list.type.blacklist"))));
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
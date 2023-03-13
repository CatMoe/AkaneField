package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;
import java.util.stream.Collectors;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.IAntiBotManager;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.utils.MessageSendUtil;
import catmoe.fallencrystal.akanefield.utils.Utils;

public class AddRemoveWhitelistCommand implements SubCommand {
    private final IAntiBotManager iAntiBotManager;

    public AddRemoveWhitelistCommand(IAntiBotPlugin iAntiBotPlugin) {
        this.iAntiBotManager = iAntiBotPlugin.getAntiBotManager();
    }

    @Override
    public String getSubCommandId() {
        return "whitelist";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        if (args[1].equalsIgnoreCase("add")) {
            iAntiBotManager.getWhitelistService().whitelist("/" + args[2]);
            iAntiBotManager.getBlackListService().unBlacklist("/" + args[2]);
            MessageSendUtil.prefixchat((ProxiedPlayer) sender,
                    MessageManager.getMessage("white-black-list.add").replace("%type%",
                            MessageManager.getMessage("white-black-list.type.whitelist"))
                            .replace("%address%", args[2]));
            if (MessageManager.WhiteBlacklistConflectTipsEnabled == true) {
                List<String> messages = MessageManager.getMessageList("white-black-list.conflect-tips.messages")
                        .stream().map(Utils::colora).collect(Collectors.toList());
                messages.forEach(sender::sendMessage);
            }
        } else {
            if (args[1].equalsIgnoreCase("remove")) {
                iAntiBotManager.getWhitelistService().unWhitelist("/" + args[2]);
                MessageSendUtil.prefixchat((ProxiedPlayer) sender,
                        MessageManager.getMessage("white-black-list.remove")
                                .replace("%type%", MessageManager.getMessage("white-black-list.type.whitelist")
                                        .replace("%address%", args[2])));
            } else {
                MessageSendUtil.prefixchat((ProxiedPlayer) sender, MessageManager.commandWrongArgument);
            }
        }
    }

    @Override
    public String getPermission() {
        return "akanefield.whitelist";
    }

    @Override
    public int argsSize() {
        return 3;
    }

    @Override
    public Map<Integer, List<String>> getTabCompleter() {
        Map<Integer, List<String>> map = new HashMap<>();
        map.put(1, Arrays.asList("add", "remove"));
        map.put(2, Collections.singletonList("<Ip address to blacklist>"));
        return map;
    }

    @Override
    public boolean allowedConsole() {
        return true;
    }
}

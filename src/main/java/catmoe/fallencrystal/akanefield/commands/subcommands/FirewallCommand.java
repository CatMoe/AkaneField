package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.utils.Utils;

public class FirewallCommand implements SubCommand {
    private static IAntiBotPlugin iAntiBotPlugin;

    public FirewallCommand(IAntiBotPlugin iAntiBotPlugin) {
        FirewallCommand.iAntiBotPlugin = iAntiBotPlugin;
    }

    public static String ReplaceInfo(String something) {
        return something
                .replace("%status%", String.valueOf(iAntiBotPlugin.getFirewallService().getFirewallStatus()))
                .replace("%blacklist%", String.valueOf(iAntiBotPlugin.getFirewallService().getBlacklistedIP()))
                .replace("%queue%", String.valueOf(iAntiBotPlugin.getFirewallService().getIPQueue()));
    }

    @Override
    public String getSubCommandId() {
        return "firewall";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        List<String> messages = MessageManager.getMessageList("firewall.status").stream()
                .map(FirewallCommand::ReplaceInfo).map(Utils::colora).collect(Collectors.toList());
        messages.forEach(sender::sendMessage);
    }

    @Override
    public String getPermission() {
        return "akanefield.firewall";
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

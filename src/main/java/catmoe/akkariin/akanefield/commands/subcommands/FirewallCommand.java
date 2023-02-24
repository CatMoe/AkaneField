package catmoe.akkariin.akanefield.commands.subcommands;

import catmoe.akkariin.akanefield.commands.SubCommand;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.service.FirewallService;
import catmoe.akkariin.akanefield.utils.Utils;
import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.Map;

public class FirewallCommand implements SubCommand {
    private final IAntiBotPlugin iAntiBotPlugin;

    public FirewallCommand(IAntiBotPlugin iAntiBotPlugin) {
        this.iAntiBotPlugin = iAntiBotPlugin;
    }

    @Override
    public String getSubCommandId() {
        return "firewall";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        FirewallService service = iAntiBotPlugin.getFirewallService();

        sender.sendMessage(
                Utils.colora("&c状态: &f%status%".replace("%status%", service.getFirewallStatus())));
        sender.sendMessage("");
        sender.sendMessage(
                Utils.colora("&c检查队列:&f %queue%".replace("%queue%", String.valueOf(service.getIPQueue()))));
        sender.sendMessage(Utils.colora("&c黑名单地址数量:&f %blacklist%").replace("%blacklist%",
                String.valueOf(service.getBlacklistedIP())));
        sender.sendMessage("");
        sender.sendMessage(Utils.colora("&b服务器将继续受到攻击 直到我们收集完所有被疑似用于攻击的IP地址.."));
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

package catmoe.akkariin.akanefield.commands.subcommands;

import catmoe.akkariin.akanefield.commands.SubCommand;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.common.utils.PasteBinBuilder;
import catmoe.akkariin.akanefield.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class DumpCommand implements SubCommand {

    private final IAntiBotPlugin plugin;

    public DumpCommand(IAntiBotPlugin iAntiBotManager) {
        this.plugin = iAntiBotManager;
    }

    @Override
    public String getSubCommandId() {
        return "dump";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        PasteBinBuilder pasteBinBuilder = PasteBinBuilder.builder();
        pasteBinBuilder.addLine("Name: " + ProxyServer.getInstance().getName());
        pasteBinBuilder.addLine("Version: " + ProxyServer.getInstance().getVersion());
        pasteBinBuilder.addLine("Online Count: " + ProxyServer.getInstance().getOnlineCount());
        pasteBinBuilder.addLine("Plugins:");
        for (Plugin plugin : ProxyServer.getInstance().getPluginManager().getPlugins()) {
            if (plugin.getDescription().getName().contains("Protocol")
                    || plugin.getDescription().getName().contains("Geyser")) {
                pasteBinBuilder.addLine(plugin.getDescription().getName() + " - " + plugin.getDescription().getVersion()
                        + " - It could cause problems!");
            } else {
                pasteBinBuilder
                        .addLine(plugin.getDescription().getName() + " - " + plugin.getDescription().getVersion());
            }
        }
        pasteBinBuilder.addLine("Plugin Info:");
        pasteBinBuilder.addLine("Version: " + plugin.getVersion());
        pasteBinBuilder.addLine("Whitelist Size: " + plugin.getAntiBotManager().getWhitelistService().size());
        pasteBinBuilder.addLine("Blacklist Size: " + plugin.getAntiBotManager().getBlackListService().size());
        pasteBinBuilder.addLine("Users: " + plugin.getUserDataService().size());
        plugin.runTask(pasteBinBuilder::pasteSync, true);
        pasteBinBuilder.pasteSync();
        sender.sendMessage(Utils.colora(MessageManager.prefix + "&fsending request to server, wait please..."));
        plugin.scheduleDelayedTask(() -> {
            if (pasteBinBuilder.isReady()) {
                sender.sendMessage(new TextComponent(Utils.colora(MessageManager.prefix + "&fDump is ready: "
                        + pasteBinBuilder.getUrl() + " &7(It will reset in a week!)")));
            }
        }, true, 2500L);
    }

    @Override
    public String getPermission() {
        return "akanefield.dump";
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

package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.PasteBinBuilder;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;

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
        pasteBinBuilder
                .addLine(MessageManager.getMessage("dump.info.proxy-name") + ProxyServer.getInstance().getName());
        pasteBinBuilder
                .addLine(MessageManager.getMessage("dump.info.proxy-version") + ProxyServer.getInstance().getVersion());
        pasteBinBuilder.addLine(
                MessageManager.getMessage("dump.info.online-players-count")
                        + ProxyServer.getInstance().getOnlineCount());
        pasteBinBuilder.addLine(MessageManager.getMessage("dump.info.plugins-list"));
        for (Plugin plugin : ProxyServer.getInstance().getPluginManager().getPlugins()) {
            pasteBinBuilder.addLine(plugin.getDescription().getName() + " - " + plugin.getDescription().getVersion());
        }
        pasteBinBuilder.addLine(MessageManager.getMessage("dump.info.plugin-info"));
        pasteBinBuilder.addLine(MessageManager.getMessage("dump.info.version") + plugin.getVersion());
        pasteBinBuilder.addLine(MessageManager.getMessage("dump.info.whitelist")
                + plugin.getAntiBotManager().getWhitelistService().size());
        pasteBinBuilder.addLine(MessageManager.getMessage("dump.info.blacklist")
                + plugin.getAntiBotManager().getBlackListService().size());
        pasteBinBuilder.addLine(
                MessageManager.getMessage("dump.info.database-player-count") + plugin.getUserDataService().size());
        plugin.runTask(pasteBinBuilder::pasteSync, true);
        pasteBinBuilder.pasteSync();
        sender.sendMessage(ServerUtil.colorize(MessageManager.prefix + MessageManager.getMessage("dump.uploading")));
        plugin.scheduleDelayedTask(() -> {
            if (pasteBinBuilder.isReady()) {
                sender.sendMessage(
                        ServerUtil.colorize(MessageManager.prefix + MessageManager.getMessage("dump.uploaded")));
                sender.sendMessage(
                        new TextComponent(ServerUtil.colorize(MessageManager.prefix + pasteBinBuilder.getUrl())));
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

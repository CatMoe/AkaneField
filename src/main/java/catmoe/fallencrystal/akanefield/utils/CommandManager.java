package catmoe.fallencrystal.akanefield.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.config.Configuration;

public final class CommandManager {
    private final CommandPlugin plugin;
    private final List<ProxyCommand> proxyJoinCommandList;

    public CommandManager(CommandPlugin plugin) {
        this.plugin = plugin;
        this.proxyJoinCommandList = new ArrayList<>();
    }

    public CommandPlugin getPlugin() {
        return this.plugin;
    }

    public List<ProxyCommand> getProxyJoinCommandList() {
        return Collections.unmodifiableList(this.proxyJoinCommandList);
    }

    public void loadProxyJoinCommands() {
        this.proxyJoinCommandList.clear();

        CommandPlugin plugin = getPlugin();
        Configuration configuration = plugin.getConfig();
        if (configuration == null) {
            return;
        }

        Configuration section = configuration.getSection("proxy-join-commands");
        if (section == null) {
            return;
        }

        Collection<String> commandIdList = section.getKeys();
        for (String commandId : commandIdList) {
            if (commandId == null || commandId.isEmpty()) {
                continue;
            }

            Configuration commandSection = section.getSection(commandId);
            if (commandSection == null) {
                continue;
            }

            ProxyCommand proxyJoinCommand = loadProxyJoinCommand(commandId, commandSection);
            if (proxyJoinCommand == null) {
                continue;
            }

            this.proxyJoinCommandList.add(proxyJoinCommand);
        }
    }

    private ProxyCommand loadProxyJoinCommand(String commandId, Configuration section) {
        if (section == null) {
            return null;
        }

        List<String> commandList = section.getStringList("command-list");
        String permission = section.getString("permission");
        long delay = section.getLong("delay");

        try {
            return new ProxyCommand(commandList, permission, delay);
        } catch (Exception ex) {
            CommandPlugin plugin = getPlugin();
            Logger logger = plugin.getLogger();
            logger.log(Level.WARNING, "An error occurred while loading the proxy join command with id '"
                    + commandId + "':", ex);
            return null;
        }
    }
}
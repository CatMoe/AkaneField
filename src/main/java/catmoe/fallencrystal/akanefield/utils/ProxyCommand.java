package catmoe.fallencrystal.akanefield.utils;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;

import org.jetbrains.annotations.NotNull;

import catmoe.fallencrystal.akanefield.common.utils.Validate;

public final class ProxyCommand {
    private final List<String> commandList;
    private final String permission;
    private final long delay;

    public ProxyCommand(List<String> commandList, String permission, long delay) {
        this.commandList = Validate.notEmpty(commandList, "commandList must not be empty!");
        this.permission = Validate.notNull(permission, "permission must not be null!");
        this.delay = delay;
    }

    @NotNull
    public List<String> getCommands() {
        return Collections.unmodifiableList(this.commandList);
    }

    @NotNull
    public String getPermission() {
        return this.permission;
    }

    public long getDelay() {
        return this.delay;
    }

    public boolean shouldBeExecutedFor(CommandPlugin plugin, ProxiedPlayer player) {
        Validate.notNull(plugin, "plugin must not be null!");
        Validate.notNull(player, "player must not be null!");

        String permission = getPermission();
        if (!permission.isEmpty()) {
            return player.hasPermission(this.permission);
        }

        return true;
    }

    public void executeFor(CommandPlugin plugin, ProxiedPlayer player) {
        Validate.notNull(plugin, "plugin must not be null!");
        Validate.notNull(player, "player must not be null!");

        String playerName = player.getName();
        List<String> commandList = getCommands();
        for (String originalCommand : commandList) {
            String replacedCommand = originalCommand.replace("{player}", playerName);
            if (replacedCommand.startsWith("[PLAYER]")) {
                String playerCommand = replacedCommand.substring(8);
                runAsPlayer(plugin, player, playerCommand);
            } else {
                runAsConsole(plugin, replacedCommand);
            }
        }
    }

    private void runAsPlayer(CommandPlugin plugin, ProxiedPlayer player, String command) {
        Validate.notNull(plugin, "plugin must not be null!");
        Validate.notNull(player, "player must not be null!");
        Validate.notEmpty(command, "command must not be empty!");

        try {
            ProxyServer proxy = plugin.getProxy();
            PluginManager manager = proxy.getPluginManager();
            manager.dispatchCommand(player, command);
        } catch (Exception ex) {
            Logger logger = plugin.getLogger();
            String errorMessage = "An error occurred while executing command '/" + command + "' as a player:";
            logger.log(Level.WARNING, errorMessage, ex);
        }
    }

    private void runAsConsole(CommandPlugin plugin, String command) {
        Validate.notNull(plugin, "plugin must not be null!");
        Validate.notEmpty(command, "command must not be empty!");

        try {
            ProxyServer proxy = plugin.getProxy();
            CommandSender console = proxy.getConsole();
            PluginManager manager = proxy.getPluginManager();
            manager.dispatchCommand(console, command);
        } catch (Exception ex) {
            Logger logger = plugin.getLogger();
            String errorMessage = "An error occurred while executing command '/" + command + "' in console:";
            logger.log(Level.WARNING, errorMessage, ex);
        }
    }
}
package catmoe.fallencrystal.akanefield.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.utils.MessageSendUtil;
import catmoe.fallencrystal.akanefield.utils.Utils;

public class CommandManager extends Command implements TabExecutor {

    private final List<SubCommand> loadedCommands;
    private final List<String> tabComplete;
    private String defaultCommandWrongArgumentMessage;
    private String noPermsMessage;
    private String noPlayerMessage;

    public CommandManager(IAntiBotPlugin iAntiBotPlugin, String name, String permission, String... aliases) {
        super(name, permission, aliases);
        this.loadedCommands = new ArrayList<>();
        this.tabComplete = new ArrayList<>();
        this.defaultCommandWrongArgumentMessage = MessageManager.commandWrongArgument;
        this.noPermsMessage = MessageManager.commandNoPerms;
        this.noPlayerMessage = MessageManager.commandPlayerOnly;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            // 由于特殊原因 我们已不允许用户修改他们使用AkaneField的事实 故无法更改此消息
            // 永远不准修改此消息中的任何内容(除非由我变动) 否则视为非法变更
            // 将违反许可证中的 "您不能将此副本声明为您自己的".
            MessageSendUtil.prefixchat((ProxiedPlayer) sender,
                    ("&aRunning &bAkane&fField &b%version%").replace("%version%", "3.0.1"));
            if (sender.hasPermission("akanefield")) {
                MessageSendUtil.prefixchat((ProxiedPlayer) sender, "&b使用 &f/af help &b来查看帮助命令");
            } else {
                MessageSendUtil.prefixchat((ProxiedPlayer) sender, "&c您没有权限使用此命令.");
            }
            return;
        }
        SubCommand cmd = getSubCommandFromArgs(args[0]);
        if (cmd == null) {
            MessageSendUtil.prefixchat((ProxiedPlayer) sender, defaultCommandWrongArgumentMessage);
            return;
        }
        if (args[0].equals(cmd.getSubCommandId()) && args.length == cmd.argsSize()) {
            if (!cmd.allowedConsole() && !(sender instanceof ProxiedPlayer)) {
                MessageSendUtil.prefixchat((ProxiedPlayer) sender, noPlayerMessage);
                return;
            }
            if (sender.hasPermission(cmd.getPermission())) {
                cmd.execute(sender, args);
            } else {
                MessageSendUtil.prefixchat((ProxiedPlayer) sender, noPermsMessage);
            }
        } else {
            MessageSendUtil.prefixchat((ProxiedPlayer) sender, defaultCommandWrongArgumentMessage);
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        SubCommand subCommand = getSubCommandFromArgs(args[0]);
        if (subCommand != null && args[0].equals(subCommand.getSubCommandId())) {
            if (subCommand.getTabCompleter() != null && subCommand.getTabCompleter().get(args.length - 1) != null) {
                return subCommand.getTabCompleter().get(args.length - 1);
            } else {
                return Utils.calculatePlayerNames();
            }
        }
        if (args.length == 1) {
            return tabComplete;
        }
        return Utils.calculatePlayerNames();
    }

    public void setDefaultCommandWrongArgumentMessage(String defaultCommandWrongArgumentMessage) {
        this.defaultCommandWrongArgumentMessage = defaultCommandWrongArgumentMessage;
    }

    public void setNoPlayerMessage(String noPlayerMessage) {
        this.noPlayerMessage = noPlayerMessage;
    }

    public void setNoPermsMessage(String noPermsMessage) {
        this.noPermsMessage = noPermsMessage;
    }

    private SubCommand getSubCommandFromArgs(String args0) {
        for (SubCommand subCommand : loadedCommands) {
            if (subCommand.getSubCommandId().equals(args0)) {
                return subCommand;
            }
        }
        return null;
    }

    public void register(SubCommand subCommand) {
        loadedCommands.add(subCommand);
        tabComplete.add(subCommand.getSubCommandId());
    }
}

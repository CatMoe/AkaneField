package catmoe.akkariin.akanefield.commands.subcommands;

import catmoe.akkariin.akanefield.commands.SubCommand;
import catmoe.akkariin.akanefield.common.IAntiBotPlugin;
import catmoe.akkariin.akanefield.common.objects.attack.AttackLog;
import catmoe.akkariin.akanefield.common.utils.MessageManager;
import catmoe.akkariin.akanefield.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * /akanefield logs list <amount>
 * /akanefield logs info <id>
 */
public class AttackLogCommand implements SubCommand {
    private final IAntiBotPlugin plugin;

    public AttackLogCommand(IAntiBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getSubCommandId() {
        return "logs";
    }

    @Override
    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        int value = -1;

        try {
            value = Integer.parseInt(args[2]);
        } catch (Exception e) {
            value = -1;
        }

        if (args[1].equals("info")) {
            Optional<AttackLog> log = plugin.getAttackTrackerService().getAttackLog(value);
            if (!log.isPresent()) {
                sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getMessage("log.empty")));
                return;
            }

            AttackLog attackLog = log.get();
            List<String> messages = MessageManager.getMessageList("log.attack-log").stream()
                    .map(attackLog::replaceInformation).map(Utils::colora).collect(Collectors.toList());
            messages.forEach(sender::sendMessage);
        }

        if (args[1].equals("list")) {
            sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getMessage("log.founding")));
            List<AttackLog> lastAttacks = plugin.getAttackTrackerService().getLastAttacks(value);
            if (lastAttacks.size() == 0) {
                sender.sendMessage(Utils.colora(MessageManager.prefix + "&fThere are no attacks to show!"));
                return;
            }
            for (AttackLog attack : lastAttacks) {
                TextComponent component = new TextComponent(
                        Utils.colora("&b" + attack.getAttackDate() + " &f[&bClick&f]"));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("§b点击查看日志§f " + attack.getID() + " §b的详细信息").create()));
                component.setClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/akanefield logs info " + attack.getID()));
                sender.sendMessage(component);
            }
        }
        try {
        } catch (IllegalArgumentException e) {
            sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getMessage("log.invalid-value")));
        }
    }

    @Override
    public String getPermission() {
        return "akanefield.logs";
    }

    @Override
    public int argsSize() {
        return 3;
    }

    @Override
    public Map<Integer, List<String>> getTabCompleter() {
        Map<Integer, List<String>> map = new HashMap<>();
        map.put(1, Arrays.asList("list", "info"));
        return map;
    }

    @Override
    public boolean allowedConsole() {
        return false;
    }
}

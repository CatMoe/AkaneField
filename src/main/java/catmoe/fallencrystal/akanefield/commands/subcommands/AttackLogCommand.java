package catmoe.fallencrystal.akanefield.commands.subcommands;

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

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.objects.attack.AttackLog;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.utils.Utils;

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
            if (value < 0) {
                sender.sendMessage(
                        Utils.colora(MessageManager.prefix + MessageManager.getMessage("log.negative-number")));
                return;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getMessage("log.non-number")));
            return;
        } catch (Exception e) {
            sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getMessage("logs.invalid-value")));
            return;
        }

        if (args[1].equals("info")) {
            Optional<AttackLog> log = plugin.getAttackTrackerService().getAttackLog(value);
            if (!log.isPresent()) {
                sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getMessage("log.empty")));
                return;
            }

            AttackLog attackLog = log.get();
            List<String> messages = MessageManager.getMessageList("log.log-info").stream()
                    .map(attackLog::replaceInformation).map(Utils::colora).collect(Collectors.toList());
            messages.forEach(sender::sendMessage);
        }

        if (args[1].equals("list")) {
            sender.sendMessage(
                    Utils.colora(MessageManager.prefix + MessageManager.getMessage("log.founding")));
            List<AttackLog> lastAttacks = plugin.getAttackTrackerService().getLastAttacks(value);
            if (lastAttacks.size() == 0) {
                sender.sendMessage(Utils.colora(MessageManager.prefix + MessageManager.getMessage("log.empty")));
                return;
            }
            for (AttackLog attack : lastAttacks) {
                TextComponent component = new TextComponent(
                        Utils.colora("&b" + attack.getAttackDate() + MessageManager.getMessage("log.clicktips")));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(Utils.colora(MessageManager.getMessage("log.clickhover"))).create()));
                component.setClickEvent(
                        new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/akanefield logs info " + attack.getID()));
                sender.sendMessage(component);
            }
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

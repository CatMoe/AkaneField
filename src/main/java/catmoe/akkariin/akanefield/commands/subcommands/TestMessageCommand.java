package catmoe.akkariin.akanefield.commands.subcommands;

import catmoe.akkariin.akanefield.commands.SubCommand;
import catmoe.akkariin.akanefield.utils.Utils;
import net.md_5.bungee.api.CommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMessageCommand implements SubCommand {
    @Override
    public String getSubCommandId() {
        return "text";
    }

    @Override
    public String getPermission() {
        return "akanefield.text";
    }

    @Override
    public int argsSize() {
        return 3;
    }

    @Override
    public Map<Integer, List<String>> getTabCompleter() {
        Map<Integer, List<String>> map = new HashMap<>();
        map.put(1, Arrays.asList("legacy", "minimsg"));
        return map;
    }

    @Override
    public boolean allowedConsole() {
        return true;
    }

    @SuppressWarnings("deprecation")
    public void execute(CommandSender sender, String[] args) {
        try {
            if (args[1].equalsIgnoreCase("legacy")) {
                sender.sendMessage(Utils.colora(args[2]));
                return;
            }
            if (args[1].equalsIgnoreCase("minimsg")) {
                sender.sendMessage(Utils.colora("unsupported"));
                return;
            }
        } catch (Exception e) {
            sender.sendMessage(Utils.colora("Cannot Parse."));
        }
    }
}

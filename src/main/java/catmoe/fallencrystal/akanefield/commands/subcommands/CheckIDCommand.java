package catmoe.fallencrystal.akanefield.commands.subcommands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.objects.profile.BlackListProfile;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.utils.MessageSendUtil;

@SuppressWarnings("deprecation")
public class CheckIDCommand implements SubCommand {

    private final IAntiBotPlugin plugin;

    public CheckIDCommand(IAntiBotPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getSubCommandId() {
        return "check";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        BlackListProfile profile = plugin.getAntiBotManager().getBlackListService().getBlacklistProfileFromID(args[1]);
        if (profile == null) {
            MessageSendUtil.prefixchat((ProxiedPlayer) sender, MessageManager.commandNoBlacklist);
            return;
        }
        for (String str : MessageManager.blacklistProfileString) {
            sender.sendMessage(ServerUtil.colorize(str
                    .replace("$reason", profile.getReason())
                    .replace("$id", profile.getId())
                    .replace("$nick", profile.getName())
                    .replace("$ip", profile.getIp())));

        }

        TextComponent whitelistComponent = new TextComponent();
        whitelistComponent.setText("§b[白名单]");
        whitelistComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                (new ComponentBuilder("§b点击将此IP加入白名单").create())));
        whitelistComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                "/akanefield whitelist add " + profile.getIp().replace("/", "")));

        TextComponent blacklistComponent = new TextComponent();
        blacklistComponent.setText("§c[黑名单]");
        blacklistComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                (new ComponentBuilder("§c点击将此IP加入黑名单").create())));
        blacklistComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                "/akanefield blacklist add " + profile.getIp().replace("/", "")));

        sender.sendMessage(whitelistComponent, blacklistComponent);
    }

    @Override
    public String getPermission() {
        return "akanefield.check";
    }

    @Override
    public int argsSize() {
        return 2;
    }

    @Override
    public Map<Integer, List<String>> getTabCompleter() {
        Map<Integer, List<String>> map = new HashMap<>();
        map.put(1, Collections.singletonList("<玩家名>"));
        return map;
    }

    @Override
    public boolean allowedConsole() {
        return false;
    }
}

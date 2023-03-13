package catmoe.fallencrystal.akanefield.commands.subcommands;

import java.util.List;
import java.util.Map;

import catmoe.fallencrystal.akanefield.commands.SubCommand;
//import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.gui.TestMenu;
//import catmoe.fallencrystal.akanefield.utils.Utils;
import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.SoundCategory;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.Sound;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TestMenuCommand implements SubCommand {

    @Override
    public String getSubCommandId() {
        return "testgui";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendTestMenuSound((ProxiedPlayer) sender);
        TestMenu menu = new TestMenu();
        menu.open((ProxiedPlayer) sender);
    }

    public static void sendTestMenuSound(ProxiedPlayer player) {
        ProtocolizePlayer proPlayer = Protocolize.playerProvider().player(player.getUniqueId());
        proPlayer.playSound(Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 1f, 1f);
    }

    @Override
    public String getPermission() {
        return "akanefield.gui";
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
        return false;
    }

}

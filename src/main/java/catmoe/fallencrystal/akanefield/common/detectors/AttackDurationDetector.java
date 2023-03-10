package catmoe.fallencrystal.akanefield.common.detectors;

import java.util.ArrayList;
import java.util.List;

import catmoe.fallencrystal.akanefield.common.IAntiBotPlugin;
import catmoe.fallencrystal.akanefield.common.utils.MessageManager;
import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AttackDurationDetector extends AbstractDetector {
    private final IAntiBotPlugin plugin;
    private static long duration;
    private static final List<ProxiedPlayer> titles = new ArrayList<>();

    public AttackDurationDetector(IAntiBotPlugin plugin) {
        this.plugin = plugin;
        AttackDurationDetector.duration = 0;
    }

    @Override
    public int getTickDelay() {
        return 1;
    }

    @Override
    public void tick() {
        if (plugin.getAntiBotManager().isSomeModeOnline()) {
            duration++;
            return;
        }

        duration = 0;
    }

    public static long getAttackDuration() {
        return duration;
    }

    public void init(IAntiBotPlugin plugin) {
        if (MessageManager.attackNotificationTitleEnabled == true) {
            if (duration == 1) {
                Title t = ProxyServer.getInstance().createTitle();
                t.title(new TextComponent(ServerUtil.colorize(MessageManager.attackNotificationTitleTitle)));
                t.subTitle(new TextComponent(ServerUtil.colorize(MessageManager.attackNotificationTitlesubTitle)));
                t.stay(MessageManager.attackNotificationTitleStay);
                t.fadeIn(MessageManager.attackNotificationTitleFadeIn);
                t.fadeOut(MessageManager.attackNotificationTitleFadeOut);
                titles.forEach(t::send);
            }
        }
    }
}

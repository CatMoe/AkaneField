package catmoe.akkariin.akanefield.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Event;

public class EventCaller {

    private EventCaller() {
    }

    public static void call(Event event) {
        ProxyServer.getInstance().getPluginManager().callEvent(event);
    }
}

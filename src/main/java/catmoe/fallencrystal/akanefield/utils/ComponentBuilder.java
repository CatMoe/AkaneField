package catmoe.fallencrystal.akanefield.utils;

import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ComponentBuilder {

    private ComponentBuilder() {
    }

    public static BaseComponent buildColorized(String str) {
        return new TextComponent(ServerUtil.colorize(str));
    }
}

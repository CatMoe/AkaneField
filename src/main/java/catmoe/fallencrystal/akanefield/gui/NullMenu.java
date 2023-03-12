package catmoe.fallencrystal.akanefield.gui;

import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.gui.util.GUIBuilder;
import dev.simplix.protocolize.data.inventory.InventoryType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class NullMenu extends GUIBuilder {
    int page = 1;

    public void update(ProxiedPlayer p) {
        clear();
        define(getPlayer());
        super.open(p);
    }

    @Override
    public void define(ProxiedPlayer p) {
        super.define(p);
        this.type(InventoryType.GENERIC_9X3);
        this.setTitle(ServerUtil.colorize("&bAkane&fField"));
    }
}

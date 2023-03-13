package catmoe.fallencrystal.akanefield.gui;

import catmoe.fallencrystal.akanefield.gui.util.GUIBuilder;
import catmoe.fallencrystal.akanefield.gui.util.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

import catmoe.fallencrystal.akanefield.common.utils.ServerUtil;
import catmoe.fallencrystal.akanefield.gui.util.ForceFormatCode;

import dev.simplix.protocolize.api.inventory.InventoryClick;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.inventory.InventoryType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TestMenu extends GUIBuilder {
    int page = 1;

    public void update() {
        clear();
        define(getPlayer());
        open(getPlayer());
    }

    @Override
    public void define(ProxiedPlayer p) {
        super.define(p);
        this.type(InventoryType.GENERIC_9X3);
        this.setTitle(ForceFormatCode.replaceFormat("&bAkane&fField"));
        setItem(13, new ItemBuilder(ItemType.GRAY_STAINED_GLASS_PANE)
                .name("&b这是一块玻璃!")
                .lore("&7\"非常抽象的无处不在\"")
                .lore("&f点击可以再次打开菜单")
                .lore("&7*虽然我能有一千种办法让你摸不到这块玻璃的真实")
                .build());
        setItem(26, new ItemBuilder(ItemType.BARRIER)
                .name("&c退出")
                .lore("&7对了 提醒你一下")
                .lore("&7这个菜单是在BungeeCord上的")
                .lore("&7这块抽象玻璃的价值超过了任何玻璃")
                .lore("&7以及造这块玻璃的材料也是(?)")
                .lore("")
                .lore("&8你可以通过点击\"空气\"来关闭菜单.")
                .build());
    }

    public void onClick(InventoryClick e) {
        // 防止玻璃没了 所以必须确认该slot是否为玻璃 (至少我认为)
        if (e.slot() == 13 && e.clickedItem().itemType() == ItemType.GRAY_STAINED_GLASS_PANE) {
            sendActionbarMessage(getPlayer(), "=w=");
            update();
            // 检不检测屏障都无所谓惹 反正朝这点准关闭菜单
        } else if (e.slot() == 26) { // 保留屏障的可执行 尽管点其它东西都会关闭菜单
            close();
        } else {
            close();
        }
    }

    public void sendActionbarMessage(ProxiedPlayer p, String string) {
        List<ProxiedPlayer> actionbars = new ArrayList<>();
        actionbars.add(p);
        p.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ServerUtil.colorize(string)));
        actionbars.remove(p);
    }

    @Override
    public void open(ProxiedPlayer p) {
        clear();
        define(p);
        super.open(p);
        sendActionbarMessage(p, "&b这是一个用BungeeCord做的菜单w!");
    }
}

package catmoe.fallencrystal.akanefield.gui.menus.main;

import catmoe.fallencrystal.akanefield.Notificator;
import catmoe.fallencrystal.akanefield.common.detectors.AttackDurationDetector;
import catmoe.fallencrystal.akanefield.common.utils.TimeUtil;
import catmoe.fallencrystal.akanefield.common.utils.resource.CPUMonitor;
import catmoe.fallencrystal.akanefield.gui.util.GUIBuilder;
import catmoe.fallencrystal.akanefield.gui.util.ItemBuilder;
import catmoe.fallencrystal.akanefield.utils.MessageSendUtil;

import dev.simplix.protocolize.api.inventory.InventoryClick;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.inventory.InventoryType;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MainMenu extends GUIBuilder {
    int page = 1;

    public void update() {
        clear();
        define(getPlayer());
    }

    @Override
    public void open(ProxiedPlayer p) {
        clear();
        define(p);
        super.open(p);
    }

    // 所以具有不一样的物品的条件里都将指定物品本身 避免出错.
    @Override
    public void onClick(InventoryClick e) {
        if (e.clickedItem().itemType() == ItemType.GRAY_STAINED_GLASS_PANE) {
            update();
        } else if (e.slot() == 10 && e.clickedItem().itemType() == ItemType.COMPASS) {
            // 物品Check
            MessageSendUtil.actionbar(getPlayer(), "&bComing soon");
            update();
        } else if (e.slot() == 12 && e.clickedItem().itemType() == ItemType.BOOK) {
            // 物品Util
            MessageSendUtil.actionbar(getPlayer(), "&bComing soon");
            update();
        } else if (e.slot() == 14 && e.clickedItem().itemType() == ItemType.PAPER) {
            // 物品Verbose
            Notificator.toggleActionBar(getPlayer());
            update();
            // 物品Reload
        } else if (e.slot() == 16 && e.clickedItem().itemType() == ItemType.REDSTONE) {
            MessageSendUtil.actionbar(getPlayer(), "&bComing soon");
            update();
        } else if (e.slot() == 20 && e.clickedItem().itemType() == ItemType.COMMAND_BLOCK) {
            MessageSendUtil.actionbar(getPlayer(), "&a已成功刷新.");
            update();
        } else if (e.slot() == 22 && e.clickedItem().itemType() == ItemType.BEACON) {
            MessageSendUtil.prefixchat(getPlayer(), "&bGithub &fhttps://github.com/CatMoe/AkaneField");
            MessageSendUtil.prefixchat(getPlayer(), "&bWebsite &fmiaomoe.net");
            MessageSendUtil.prefixchat(getPlayer(), "&b猫萌服务器IP mc.miaomoe.net");
            update();
        } else if (e.slot() == 24 && e.clickedItem().itemType() == ItemType.ENDER_PEARL) {
            update();
        } else if (e.slot() == 31 && e.clickedItem().itemType() == ItemType.BARRIER) {
            close();
        } else if (e.clickedItem() == null) {
            close();
        } else {
            close();
        }
    }

    public void TrashPane(int slot, String nativename) {
        setItem(slot, new ItemBuilder(ItemType.GRAY_STAINED_GLASS_PANE)
                .name(nativename)
                .build());
    }

    @Override
    public void define(ProxiedPlayer p) {
        super.define(p);
        this.type(InventoryType.GENERIC_9X5);
        this.setTitle("AkaneField");

        // 指南针 slot10 y2 x2 gui的slot从0开始算起
        // new ItemBuilder为自己的util类挂钩
        // catmoe.fallencrystal.akanefield.gui.util.GUIBuilder;
        setItem(10, new ItemBuilder(ItemType.COMPASS)
                .name("&bCheck")
                .build() // 最终生成物品
        );

        setItem(12, new ItemBuilder(ItemType.BOOK)
                .name("&bUtil")
                .build());

        setItem(14, new ItemBuilder(ItemType.PAPER)
                .name("&bVerbose")
                .lore("")
                .lore("&f点击来切换actionbar")
                .build());

        setItem(16, new ItemBuilder(ItemType.REDSTONE)
                .name("&cReload")
                .lore("")
                .lore("&7重载插件")
                .lore("&c需要安装BungeePluginManagerPlus!")
                .build());

        setItem(20, new ItemBuilder(ItemType.COMMAND_BLOCK)
                .name("&b服务器信息")
                .lore("&aBungeeCord信息 >")
                .lore(" &b代理名: &f" + ProxyServer.getInstance().getName())
                .lore(" &b版本: &f" + ProxyServer.getInstance().getVersion())
                .lore(" &b在线玩家: &f" + ProxyServer.getInstance().getOnlineCount())
                .lore(" &bCPU使用率: &f" + CPUMonitor.recentProcessCpuLoadSnapshot)
                .lore("")
                .lore("&bAkane&fField &a信息 >")
                .lore(" &b攻击持续时间: &f" + TimeUtil.formatSeconds(AttackDurationDetector.getAttackDuration()))
                .lore(" &bMore Coming soon.")
                // .lore(" &b防火墙封禁数量: &f" + (FirewallService.getBlacklistedIP()))
                .lore("")
                .lore("&7 > 点击来刷新菜单!")
                .build());

        setItem(22, new ItemBuilder(ItemType.BEACON)
                .name("&b相关链接")
                .build());

        setItem(24, new ItemBuilder(ItemType.ENDER_PEARL)
                .name("&bPremium User")
                .lore("&f直到目前为止还没有付费版本w!")
                .lore("&7其实是因为我不知道写什么惹ww")
                .build());

        setItem(31, new ItemBuilder(ItemType.BARRIER)
                .name("&cClose")
                .build());

        // 垃圾玻璃板 整这么麻烦是防止玩家shift+双击给玻璃板全收纳了
        TrashPane(0, "&a&a");
        TrashPane(1, "&a&b");
        TrashPane(2, "&a&c");
        TrashPane(3, "&a&d");
        TrashPane(4, "&a&e");
        TrashPane(5, "&a&f");
        TrashPane(6, "&a&1");
        TrashPane(7, "&a&2");
        TrashPane(8, "&a&3");
        TrashPane(9, "&a&4");
        // 非垃圾物品
        TrashPane(11, "&a&5");
        // 非垃圾物品
        TrashPane(13, "&a&6");
        // 非垃圾物品
        TrashPane(15, "&a&7");
        // 非垃圾物品
        TrashPane(17, "&a&8");
        TrashPane(18, "&a&9");
        TrashPane(19, "&b&a");
        // 非垃圾物品
        TrashPane(21, "&b&b");
        // 非垃圾物品
        TrashPane(23, "&b&c");
        // 非垃圾物品
        TrashPane(25, "&b&d");
        TrashPane(26, "&b&e");
        TrashPane(27, "&b&f");
        TrashPane(28, "&b&1");
        TrashPane(29, "&b&2");
        TrashPane(30, "&b&3");
        // 非垃圾物品
        TrashPane(32, "&b&4");
        TrashPane(33, "&b&5");
        TrashPane(34, "&b&6");
        TrashPane(35, "&b&7");
        TrashPane(36, "&b&8");
        TrashPane(37, "&b&9");
        TrashPane(38, "&c&a");
        TrashPane(39, "&c&b");
        TrashPane(40, "&c&c");
        TrashPane(41, "&c&d");
        TrashPane(42, "&c&e");
        TrashPane(43, "&c&f");
        TrashPane(44, "&c&1");
    }
}

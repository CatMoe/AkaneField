package catmoe.fallencrystal.akanefield.gui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dev.simplix.protocolize.api.Protocolize;
import dev.simplix.protocolize.api.inventory.Inventory;
import dev.simplix.protocolize.api.inventory.InventoryClick;
import dev.simplix.protocolize.api.inventory.InventoryClose;
import dev.simplix.protocolize.api.inventory.PlayerInventory;
import dev.simplix.protocolize.api.item.ItemStack;
import dev.simplix.protocolize.api.player.ProtocolizePlayer;
import dev.simplix.protocolize.data.ItemType;
import dev.simplix.protocolize.data.inventory.InventoryType;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class GUIBuilder {
    private InventoryType type;
    private ProxiedPlayer player;
    private HashMap<Integer, ItemStack> items = new HashMap<>();
    private List<ItemStack> emptyItems = new ArrayList<>();
    private String title;

    public void type(InventoryType type) {
        this.type = type;
    }

    public InventoryType type() {
        return this.type;
    }

    public void define(ProxiedPlayer p) {
        this.player = p;
    }

    public void setTitle(String string) {
        this.title = string;
    }

    public Inventory build() {
        Inventory inv = new Inventory(type);
        inv.title(title);
        if (!emptyItems.isEmpty()) {
            inv.items();
        }
        for (Integer index : items.keySet()) {
            ItemStack item = items.get(index);
            inv.item(index, item);
        }
        return inv;
    }

    public void open(ProxiedPlayer player) {
        Inventory i = build();
        i.onClick(this::onClick);
        i.onClose(this::onClose);
        ProtocolizePlayer Protocolplayer = Protocolize.playerProvider().player(player.getUniqueId());
        Protocolplayer.openInventory(i);
    }

    public void updateItems() {
        ProtocolizePlayer protocolizePlayer = Protocolize.playerProvider().player(player.getUniqueId());
        PlayerInventory inv = protocolizePlayer.proxyInventory();
        inv.clear();
        for (Integer index : items.keySet()) {
            ItemStack item = items.get(index);
            inv.item(index, item);
        }
        inv.update();
    }

    public InventoryType getInventoryType(int value) {
        switch (value) {
            case 0:
                return InventoryType.GENERIC_3X3;
            case 1:
                return InventoryType.GENERIC_9X1;
            case 2:
                return InventoryType.GENERIC_9X2;
            case 3:
                return InventoryType.GENERIC_9X3;
            case 4:
                return InventoryType.GENERIC_9X4;
            case 5:
                return InventoryType.GENERIC_9X5;
            case 6:
                return InventoryType.GENERIC_9X6;
            default:
                return InventoryType.GENERIC_9X3;
        }
    }

    public ItemStack getSlot(int slot) {
        return items.get(slot);
    }

    public void onClose(InventoryClose close) {
    }

    public void onClick(InventoryClick click) {
    }

    public void close() {
        ProtocolizePlayer protocolizePlayer = Protocolize.playerProvider().player(player.getUniqueId());
        protocolizePlayer.closeInventory();
    }

    protected void setEmpty(ItemType itemtype) {
        ItemStack item = new ItemBuilder(itemtype).amount(1).name("").build();
        int totalSlots = type.getTypicalSize(player.getPendingConnection().getVersion());
        for (int i = 0; i < totalSlots; i++) {
            emptyItems.add(item);
        }
    }

    protected void setItem(int index, ItemStack itemBuilder) {
        if (itemBuilder != null) {
            items.put(index, itemBuilder);
        }
    }

    protected void removeItem(int index) {
        items.remove(index);
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public void setPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    public void clear() {
        this.items.clear();
    }
}

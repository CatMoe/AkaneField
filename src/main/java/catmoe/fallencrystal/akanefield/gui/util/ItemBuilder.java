package catmoe.fallencrystal.akanefield.gui.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dev.simplix.protocolize.api.item.BaseItemStack;
import dev.simplix.protocolize.api.item.ItemStack;
import dev.simplix.protocolize.data.ItemType;
import net.md_5.bungee.api.chat.TextComponent;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.IntTag;
import net.querz.nbt.tag.ListTag;

@SuppressWarnings("unchecked")
public class ItemBuilder {
    private ItemStack item;
    private List<GUIEnchantUtil> enchantments = new ArrayList<>();
    private boolean hideEnchants;
    private List<CompoundTag> tags = new ArrayList<>();

    public ItemBuilder(ItemType material) {
        item = new ItemStack(material);
    }

    public ItemBuilder type(ItemType material) {
        item.itemType(material);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.amount((byte) amount);
        return this;
    }

    public ItemBuilder name(TextComponent textComponent) {
        item.displayName(textComponent);
        return this;
    }

    public ItemBuilder lore(TextComponent lore) {
        item.addToLore(lore);
        return this;
    }

    public ItemBuilder lore(List<TextComponent> lores) {
        lores.forEach(this::lore);
        return this;
    }

    public ItemBuilder lores(TextComponent[] lores) {
        Arrays.stream(lores).collect(Collectors.toList()).forEach(this::lore);
        return this;
    }

    public ItemBuilder durability(short durability) {
        item.durability(durability);
        return this;
    }

    public ItemBuilder enchantment(GUIEnchantUtil enchantment) {
        enchantments.add(enchantment);
        return this;
    }

    public ItemBuilder enchantments(GUIEnchantUtil[] enchantments) {
        this.enchantments.clear();
        this.enchantments.addAll(Arrays.asList(enchantments));
        return this;
    }

    public ItemBuilder enchantments(GUIEnchantsList[] enchantment, int level) {
        this.enchantments.clear();
        Arrays.asList(enchantment).stream().forEach(e -> enchantment(e, level));
        return this;
    }

    public ItemBuilder enchantment(GUIEnchantsList enchantment, int level) {
        this.enchantments.add(new GUIEnchantUtil(enchantment, level));
        return this;
    }

    public ItemBuilder clearEnchantment(GUIEnchantsList enchantments) {
        List<GUIEnchantUtil> temp = new ArrayList<>(this.enchantments);
        temp.forEach(en -> {
            if (en.getEnchantment() == enchantments)
                this.enchantments.remove(en);
        });
        return this;
    }

    public ItemBuilder clearEnchantments() {
        this.enchantments.clear();
        return this;
    }

    public ItemBuilder clearLore(TextComponent c) {
        if (item.lore().contains(c)) {
            item.lore().remove(c);
        }
        return this;
    }

    public ItemBuilder clearLores() {
        item.lore(new ArrayList<>(), false);
        return this;
    }

    public ItemBuilder clearLores(int i) {
        List<TextComponent> newList = item.lore();
        newList.subList(i, newList.size()).clear();
        item.lore(newList, true);
        return this;
    }

    public ItemBuilder clearLores(int i1, int i2) {
        List<TextComponent> newList = item.lore();
        newList.subList(i1, i2).clear();
        item.lore(newList, true);
        return this;
    }

    // public ItemBuilder color(Color color) {
    // if (item.itemType() == ItemType.LEATHER_HELMET ||
    // item.itemType() == ItemType.LEATHER_CHESTPLATE ||
    // item.itemType() == ItemType.LEATHER_LEGGINGS ||
    // item.itemType() == ItemType.LEATHER_BOOTS) {
    //
    // }
    // }

    public ItemBuilder skullOwner(String name) {
        return this;
    }

    public ItemBuilder hideEnchantments(boolean hide) {
        hideEnchants = hide;
        return this;
    }

    public ItemBuilder addTag(CompoundTag tag) {
        tags.add(tag);
        return this;
    }

    public ItemBuilder setTags(List<CompoundTag> tags) {
        this.tags = tags;
        return this;
    }

    public ItemBuilder clearTags() {
        tags.clear();
        return this;
    }

    public List<GUIEnchantUtil> getEnchantments() {
        return enchantments;
    }

    public static ItemBuilder of(ItemBuilder builder) {
        BaseItemStack item = builder.item;
        return new ItemBuilder(item.itemType())
                .lore(item.lore().stream().map(e -> (TextComponent) e).collect(Collectors.toList()))
                .enchantments(builder.enchantments.toArray(new GUIEnchantUtil[0]))
                .name(item.displayName())
                .amount(item.amount());
    }

    public static ItemBuilder from(BaseItemStack item) {
        ItemBuilder builder = new ItemBuilder(item.itemType());
        builder.name(item.displayName());
        builder.lore(item.lore());
        if (item.nbtData().get("Enchantments") != null) {
            ListTag<CompoundTag> enchants = (ListTag<CompoundTag>) item.nbtData().getListTag("Enchantments");
            for (CompoundTag tag : enchants) {
                builder.enchantment(
                        GUIEnchantsList.valueOf(tag.getString("id").replace("minecraft:", "").toUpperCase()),
                        tag.getInt("lvl"));
            }
        }
        return builder;
    }

    public ItemStack build() {
        CompoundTag nbt = item.nbtData();
        if (!enchantments.isEmpty()) {
            ListTag<CompoundTag> enchantments = (ListTag<CompoundTag>) ListTag.createUnchecked(CompoundTag.class);
            for (int i = 0; i < this.enchantments.size(); i++) {
                GUIEnchantUtil enchantment = this.enchantments.get(i);
                CompoundTag enchant = new CompoundTag();
                enchant.putString("id", enchantment.getEnchantment().getString());
                enchant.putInt("lvl", enchantment.getLevel());
                enchantments.add(enchant);
            }
            nbt.put("Enchantments", enchantments);
            if (hideEnchants)
                nbt.put("HideFlags", new IntTag(99));
            if (tags.size() > 0) {
                ListTag<CompoundTag> customTags = (ListTag<CompoundTag>) ListTag.createUnchecked(CompoundTag.class);
                customTags.addAll(tags);
                nbt.put("custom", customTags);
            }
        }
        item.nbtData(nbt);
        return item;
    }
}

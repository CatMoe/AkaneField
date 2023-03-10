package catmoe.akkariin.akanefield.gui.util;

public class GUIEnchantUtil {
    private GUIEnchantsList enchantment;
    private int level;

    public GUIEnchantUtil(GUIEnchantsList enchantment) {
        this(enchantment, 1);
    }

    public GUIEnchantUtil(GUIEnchantsList enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public GUIEnchantsList getEnchantment() {
        return enchantment;
    }

    public int getLevel() {
        return level;
    }

    public String toString() {
        return "d: \"" + enchantment.getString() + "\", lvl: " + level + "s";
    }
}

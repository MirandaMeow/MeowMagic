package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public class MagicScroll {
    private static final HashMap<String, MagicScroll> scrollHashMap = new HashMap<>();
    private final ItemStack scroll;

    /**
     * 初始化技能书
     *
     * @param skillID 技能 ID
     */
    public MagicScroll(String skillID) {
        Skill skill = Skill.getSkill(skillID);
        this.scroll = new ItemStack(Material.PAPER, 1);
        ItemMeta itemMeta = this.scroll.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(String.format("§c§l%s §r§9技能书", skill.skillName));
        List<String> lore = skill.getDescription(null, -1);
        lore.remove(0);
        itemMeta.setLore(lore);
        this.scroll.setItemMeta(itemMeta);
    }

    /**
     * 将技能书给玩家
     *
     * @param player 玩家
     */
    public void toPlayer(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            MessageManager.Message(player, Notify.INVENTORY_FULL.string);
        } else {
            player.getInventory().addItem(this.scroll);
        }
    }

    /**
     * 获取技能书实例（单例模式）
     *
     * @param skillID 技能 ID
     */
    public static MagicScroll getScroll(String skillID) {
        if (scrollHashMap.containsKey(skillID)) {
            return scrollHashMap.get(skillID);
        } else {
            MagicScroll scroll = new MagicScroll(skillID);
            scrollHashMap.put(skillID, scroll);
            return scroll;
        }
    }
}

package cn.miranda.MeowMagic.Core;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SkillPanel {
    private final Inventory panel = Bukkit.createInventory(null, 54, "§9技能面板");

    public SkillPanel(Player player) {
        int[] pink = {0, 2, 4, 6, 8, 18, 26, 36, 44, 46, 48, 50, 52};
        int[] white = {1, 3, 5, 7, 9, 17, 27, 35, 45, 47, 49, 51, 53};
        ItemStack pinkPane = new ItemStack(Material.PINK_STAINED_GLASS_PANE, 1);
        ItemStack whitePane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        for (int i : pink) {
            this.panel.setItem(i, pinkPane);
        }
        for (int i : white) {
            this.panel.setItem(i, whitePane);
        }
        User user = User.getUser(player);
        for (String skillID : user.skillState.skillLevel.keySet()) {
            Skill skill = Skill.getSkill(skillID);
            ItemStack skillIcon = skill.icon;
            List<String> lore = skill.getDescription(player);
            lore.remove(0);
            ItemMeta iconMeta = skillIcon.getItemMeta();
            assert iconMeta != null;
            iconMeta.setLore(lore);
            iconMeta.setDisplayName("§c§l" + skill.skillName);
            skillIcon.setItemMeta(iconMeta);
            this.panel.setItem(this.panel.firstEmpty(), skillIcon);
        }
    }

    public Inventory getPanel() {
        return this.panel;
    }
}

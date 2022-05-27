package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Timer.UpdateInfoPanel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.List;

public class SkillPanel {
    public final static HashMap<Player, UpdateInfoPanel> updateInfo = new HashMap<>();
    private final Inventory panel;
    private final Player player;

    /**
     * 初始化技能面板
     *
     * @param player 玩家
     */
    public SkillPanel(Player player) {
        this.player = player;
        int[] pink = {0, 2, 4, 6, 8, 18, 26, 36, 44, 46, 48, 50, 52};
        int[] white = {1, 3, 5, 7, 9, 17, 27, 35, 45, 47, 49, 51, 53};
        ItemStack pinkPanelBanner = new ItemStack(Material.PINK_STAINED_GLASS_PANE, 1);
        ItemStack whitePanelBanner = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
        ItemStack cancelButton = new ItemStack(Material.BARRIER, 1);
        this.setItemName(pinkPanelBanner, "§9技能面板");
        this.setItemName(whitePanelBanner, "§9技能面板");
        this.setItemName(cancelButton, "§c取消显示");
        this.panel = Bukkit.createInventory(null, 54, String.format("§9技能面板 - §d%s", this.player.getName()));
        for (int i : pink) {
            this.panel.setItem(i, pinkPanelBanner);
        }
        for (int i : white) {
            this.panel.setItem(i, whitePanelBanner);
        }
        this.panel.setItem(43, cancelButton);
        User user = User.getUser(player);
        for (String skillID : user.skillState.skillLevel.keySet()) {
            Skill skill = Skill.getSkill(skillID);
            ItemStack skillIcon = skill.icon;
            List<String> lore = skill.getDescription(player, user.skillState.getLevel(skillID));
            lore.remove(0);
            ItemMeta iconMeta = skillIcon.getItemMeta();
            assert iconMeta != null;
            iconMeta.setLore(lore);
            iconMeta.setDisplayName("§c§l" + skill.skillName);
            skillIcon.setItemMeta(iconMeta);
            this.panel.setItem(this.panel.firstEmpty(), skillIcon);
        }
    }

    /**
     * 设置物品的显示名称
     *
     * @param itemStack 将要被设置名称的物品
     * @param name      物品显示名称
     */
    private void setItemName(ItemStack itemStack, String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
    }

    /**
     * 获取技能面板
     *
     * @return 面板
     */
    public Inventory getPanel() {
        return this.panel;
    }

    /**
     * 打开技能面板
     */
    public void openPanel() {
        this.player.openInventory(this.panel);
    }

    /**
     * 在计分板显示技能升级信息
     *
     * @param skillID 技能 ID
     */
    public void showSkillUpdateInfo(String skillID) {
        if (updateInfo.get(this.player) != null) {
            updateInfo.get(this.player).stopTick();
        }
        updateInfo.put(player, new UpdateInfoPanel(player, skillID));
    }

    /**
     * 关闭技能升级信息
     */
    public void closeSkillUpdateInfo() {
        Scoreboard scoreboard = this.player.getScoreboard();
        scoreboard.clearSlot(DisplaySlot.SIDEBAR);
        UpdateInfoPanel updateInfoPanel = updateInfo.get(this.player);
        if (updateInfoPanel != null) {
            updateInfoPanel.stopTick();
        }
    }
}

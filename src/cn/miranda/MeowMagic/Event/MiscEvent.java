package cn.miranda.MeowMagic.Event;

import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Core.SkillPanel;
import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class MiscEvent implements Listener {
    /**
     * 当玩家登录时使该玩家自动回复魔法
     *
     * @param event 玩家登录事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void PlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MeowMagic.users.put(player, new User(player));
    }

    /**
     * 禁用怪物掉落经验球，玩家死亡仍然会掉落经验球
     *
     * @param event 实体死亡事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void BanDropExp(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            return;
        }
        event.setDroppedExp(0);
    }

    /**
     * 技能面板点击
     *
     * @param event 物品栏点击事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void ClickSkillPanel(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("§9技能面板")) {
            return;
        }
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getViewers().get(0);
        String name = clicked.getItemMeta().getDisplayName().replace("§c§l", "");
        String skillID = Skill.getSkillIDByName(name);
        SkillPanel panel = new SkillPanel(player);
        if (skillID == null) {
            panel.closeSkillUpdateInfo();
        } else {
            panel.showSkillUpdateInfo(skillID);
        }
    }

    /**
     * 禁用方块掉落经验球
     *
     * @param event 方块破坏事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void BanBlockDropExp(BlockBreakEvent event) {
        event.setExpToDrop(0);
    }
}

package cn.miranda.MeowMagic.Event;

import cn.miranda.MeowMagic.Core.Notify;
import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Core.SkillPanel;
import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.MeowMagic;
import cn.miranda.MeowMagic.Timer.Skill.ArrowShootTicker;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
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
     * 禁用怪物掉落经验球
     *
     * @param event 实体死亡事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void BanDropExp(EntityDeathEvent event) {
        event.setDroppedExp(0);
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
     * 处理附魔事件
     *
     * @param event 物品附魔事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void EnchantmentHack(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        Inventory inventory = player.getInventory();
        int enchantmentLevel = event.getExpLevelCost();
        if (deleteItem(inventory, enchantmentLevel)) {
            MessageManager.Message(player, String.format(Notify.ENCHANT_SUCCESS.string, enchantmentLevel));
        } else {
            MessageManager.Message(player, Notify.ENCHANT_FAIL.string);
            event.setCancelled(true);
        }
    }

    /**
     * @param event 玩家得到经验值事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void UpdateSkillPanel(PlayerGainSkillExp event) {
        Player player = event.getPlayer();
        InventoryView inventoryView = player.getOpenInventory();
        if (!inventoryView.getTitle().equals("§9技能面板")) {
            return;
        }
        new SkillPanel(player).openPanel();
    }

    /**
     * 刷新玩家经验条事件
     *
     * @param event 玩家经验条变更事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void ReScaleExp(PlayerExpChangeEvent event) {
        User user = User.getUser(event.getPlayer());
        user.refreshExpBar();
    }

    /**
     * 禁止玩家拾取由箭雨生成的箭
     *
     * @param event 玩家拾取箭事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void BanArrowPick(PlayerPickupArrowEvent event) {
        if (ArrowShootTicker.arrowIDs.contains(String.valueOf(event.getArrow().getEntityId()))) {
            event.setCancelled(true);
        }
    }

    /**
     * 删除绿宝石
     *
     * @param inventory 玩家的背包
     * @param count     删除数量
     * @return 返回是否成功删除
     */
    private boolean deleteItem(Inventory inventory, int count) {
        for (ItemStack itemStack : inventory) {
            if (itemStack == null) {
                continue;
            }
            if (!itemStack.getType().equals(Material.EMERALD)) {
                continue;
            }
            if (itemStack.getAmount() < count) {
                continue;
            }
            itemStack.setAmount(itemStack.getAmount() - count);
            return true;
        }
        return false;
    }
}

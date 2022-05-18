package cn.miranda.MeowMagic.Event;

import cn.miranda.MeowMagic.Core.Notify;
import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.Manager.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlayerSkillInvokerEvent implements Listener {
    /**
     * 玩家交互方块触发技能监听器
     *
     * @param event 玩家交互事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void PlayerSkillInvoker_Interact(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = User.getUser(player);
        Material mainHand = player.getInventory().getItemInMainHand().getType();
        Material offHand = player.getInventory().getItemInOffHand().getType();
        boolean sneak = player.isSneaking();
        Action action = event.getAction();
        List<Skill> skills = new ArrayList<>();
        for (Map.Entry<String, Skill> entry : Skill.skillMap.entrySet()) {
            if (entry.getValue().mainHand.contains(mainHand)) {
                skills.add(entry.getValue());
            }
        }
        if (skills.isEmpty()) {
            return;
        }
        for (Skill skill : skills) {
            if (event.getHand() != EquipmentSlot.HAND) {
                return;
            }
            if (!skill.isPositive) {
                return;
            }
            String skillName = skill.skillName;
            if (skill.sneak != sneak) {
                return;
            }
            if (!skill.click.contains(action)) {
                return;
            }
            if (!user.skillState.hasSkill(skill.skillID)) {
                return;
            }
            if (!Objects.equals(skill.invokeType, "interact")) {
                return;
            }
            int level = user.skillState.skillLevel.get(skill.skillID);
            int cost = skill.cost.get(level);
            if (!user.manaCheck(cost)) {
                MessageManager.ActionBarMessage(player, String.format(Notify.NO_MANA.string, skillName, cost));
                event.setCancelled(true);
                return;
            }
            int coolDown = user.skillState.checkCoolDown(skill.skillID);
            if (coolDown > 0) {
                MessageManager.ActionBarMessage(player, String.format(Notify.COOL_DOWN.string, skillName, coolDown));
                event.setCancelled(true);
                return;
            }
            if (skill.offhand != null) {
                if (!skill.offhand.contains(offHand)) {
                    MessageManager.ActionBarMessage(player, String.format(Notify.NEED_OFF_HAND.string, skillName, skill.offHandItemName, skill.offhandCost));
                    event.setCancelled(true);
                    return;
                } else {
                    boolean result = handleOffHandItemStack(player, offHand, skill.offhandCost, false);
                    if (!result) {
                        MessageManager.ActionBarMessage(player, String.format(Notify.NEED_OFF_HAND.string, skillName, skill.offHandItemName, skill.offhandCost));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            Random random = new Random();
            int random_int = random.nextInt(100);
            skill.update(skill.skillID, player, user.skillState);
            int chance = skill.chance.get(level);
            if (random_int > chance) {
                MessageManager.ActionBarMessage(player, String.format(Notify.FAILED.string, skillName));
                user.skillState.doCoolDown(skill.skillID);
                event.setCancelled(true);
                return;
            }
            MessageManager.ActionBarMessage(player, String.format(Notify.ACTIVE.string, skillName));
            boolean result = user.skillState.doSkill(skill.skillID, "interact", null);
            if (result) {
                user.reduceMana(cost);
                handleOffHandItemStack(player, offHand, skill.offhandCost, true);
            }
            event.setCancelled(true);
        }
    }

    /**
     * 玩家交互实体触发技能监听器
     *
     * @param event 玩家交互实体事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void PlayerSkillInvoker_InteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        User user = User.getUser(player);
        Entity target = event.getRightClicked();
        Material mainHand = player.getInventory().getItemInMainHand().getType();
        Material offHand = player.getInventory().getItemInOffHand().getType();
        boolean sneak = player.isSneaking();
        List<Skill> skills = new ArrayList<>();
        for (Map.Entry<String, Skill> entry : Skill.skillMap.entrySet()) {
            if (entry.getValue().mainHand.contains(mainHand)) {
                skills.add(entry.getValue());
            }
        }
        if (skills.isEmpty()) {
            return;
        }
        for (Skill skill : skills) {
            if (event.getHand() != EquipmentSlot.HAND) {
                return;
            }
            if (!skill.isPositive) {
                return;
            }
            String skillName = skill.skillName;
            if (skill.sneak != sneak) {
                return;
            }
            if (!user.skillState.hasSkill(skill.skillID)) {
                return;
            }
            if (!Objects.equals(skill.invokeType, "interactEntity")) {
                return;
            }
            int level = user.skillState.skillLevel.get(skill.skillID);
            int cost = skill.cost.get(level);
            if (!user.manaCheck(cost)) {
                MessageManager.ActionBarMessage(player, String.format(Notify.NO_MANA.string, skillName, cost));
                event.setCancelled(true);
                return;
            }
            int coolDown = user.skillState.checkCoolDown(skill.skillID);
            if (coolDown > 0) {
                MessageManager.ActionBarMessage(player, String.format(Notify.COOL_DOWN.string, skillName, coolDown));
                event.setCancelled(true);
                return;
            }
            if (skill.offhand != null) {
                if (!skill.offhand.contains(offHand)) {
                    MessageManager.ActionBarMessage(player, String.format(Notify.NEED_OFF_HAND.string, skillName, skill.offHandItemName, skill.offhandCost));
                    event.setCancelled(true);
                    return;
                } else {
                    boolean result = handleOffHandItemStack(player, offHand, skill.offhandCost, false);
                    if (!result) {
                        MessageManager.ActionBarMessage(player, String.format(Notify.NEED_OFF_HAND.string, skillName, skill.offHandItemName, skill.offhandCost));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            Random random = new Random();
            int random_int = random.nextInt(100);
            skill.update(skill.skillID, player, user.skillState);
            int chance = skill.chance.get(level);
            if (random_int > chance) {
                MessageManager.ActionBarMessage(player, String.format(Notify.FAILED.string, skillName));
                user.skillState.doCoolDown(skill.skillID);
                event.setCancelled(true);
                return;
            }
            MessageManager.ActionBarMessage(player, String.format(Notify.ACTIVE.string, skillName));
            boolean result = user.skillState.doSkill(skill.skillID, "interactEntity", target);
            if (result) {
                user.reduceMana(cost);
                handleOffHandItemStack(player, offHand, skill.offhandCost, true);
            }
            event.setCancelled(true);
        }
    }


    /**
     * 检查副手物品是否符合要求，如果符合则扣除
     *
     * @param player   玩家
     * @param material 物品材质
     * @param count    扣除物品数量
     * @param isDelete 是否需要删除物品
     * @return 是否扣除成功
     */
    private boolean handleOffHandItemStack(Player player, Material material, int count, boolean isDelete) {
        ItemStack offHandItemStack = player.getInventory().getItemInOffHand();
        if (offHandItemStack.getType() != material) {
            return false;
        }
        if (offHandItemStack.getAmount() < count) {
            return false;
        }
        if (isDelete) {
            offHandItemStack.setAmount(offHandItemStack.getAmount() - count);
        }
        return true;
    }
}

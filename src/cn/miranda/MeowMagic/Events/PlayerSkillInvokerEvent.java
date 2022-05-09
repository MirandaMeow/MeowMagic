package cn.miranda.MeowMagic.Events;

import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.Manager.MessageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Map;

public class PlayerSkillInvokerEvent implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void PlayerSkillInvoker(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = User.getUser(player);
        Material userHand = player.getInventory().getItemInMainHand().getType();
        boolean sneak = player.isSneaking();
        Action action = event.getAction();
        Skill skill = null;
        for (Map.Entry<String, Skill> entry : Skill.skillMap.entrySet()) {
            if (entry.getValue().itemList.contains(userHand)) {
                skill = entry.getValue();
                break;
            }
        }
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (skill == null) {
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
        int level = user.skillState.skillList.get(skill.skillID);
        assert skill.cost != null;
        int cost = skill.cost.get(level);
        if (!user.manaCheck(cost)) {
            MessageManager.ActionBarMessage(player, String.format("魔法值不足, 需要 %d", cost));
            return;
        }
        int coolDown = user.skillState.checkCoolDown(skill.skillID);
        if (coolDown > 0) {
            MessageManager.ActionBarMessage(player, String.format("%s 还未冷却, 剩余 %d 秒", skillName, coolDown));
            return;
        }
        user.reduceMana(cost);
        user.skillState.fireSkill(skill.skillID);
        MessageManager.HoverMessage(player, skillName, skill.getDescription(level));
        event.setCancelled(true);
    }
}

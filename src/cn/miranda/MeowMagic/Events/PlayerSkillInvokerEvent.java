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
        if (skill == null) {
            System.out.print("not skill\n");
            return;
        }
        String skillName = skill.skillName;
        if (skill.sneak != sneak) {
            System.out.print("no sneak\n");
            return;
        }
        if (!skill.click.contains(action)) {
            System.out.print("no action\n");
            return;
        }
        if (!user.skillState.hasSkill(skill.skillID)) {
            System.out.print("no skill\n");
            return;
        }
        int level = user.skillState.skillList.get(skill.skillID);
        assert skill.cost != null;
        int cost = skill.cost.get(level);

        if (!user.manaCheck(cost)) {
            System.out.print("no mana\n");
        }
        user.reduceMana(cost);
        user.skillState.fireSkill(skill.skillID);
        MessageManager.HoverMessage(player, skill.skillName, skill.getDescription(level));
        event.setCancelled(true);
    }
}

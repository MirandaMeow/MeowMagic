package cn.miranda.MeowMagic.Timer;

import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class CoolDown {
    private final HashMap<String, Integer> skillCoolDown;

    /**
     * 冷却技能定时器
     *
     * @param skillCoolDown 技能和冷却时间 Hashmap
     * @param player        玩家
     */
    public CoolDown(HashMap<String, Integer> skillCoolDown, Player player) {
        this.skillCoolDown = skillCoolDown;
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            for (Map.Entry<String, Integer> entry : this.skillCoolDown.entrySet()) {
                String skillID = entry.getKey();
                int coolDown = entry.getValue();
                if (coolDown > 1) {
                    this.skillCoolDown.put(skillID, coolDown - 1);
                } else {
                    if (coolDown == 1) {
                        MessageManager.ActionBarMessage(player, String.format("§c§l%s§r§e冷却完成", Skill.getSkill(skillID).skillName));
                        entry.setValue(-1);
                    }
                }
            }
        }, 0L, 20L);
    }

    /**
     * 清除所有冷却时间
     */
    public void clear() {
        this.skillCoolDown.replaceAll((k, v) -> -1);
    }
}

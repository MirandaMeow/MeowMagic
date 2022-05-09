package cn.miranda.MeowMagic.Timer;

import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoolDown {
    private final HashMap<String, Integer> skillCoolDown;

    public CoolDown(HashMap<String, Integer> skillCoolDown, Player player) {
        this.skillCoolDown = skillCoolDown;
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            for (Map.Entry<String, Integer> entry : skillCoolDown.entrySet()) {
                String skillID = entry.getKey();
                int coolDown = entry.getValue();
                if (coolDown > 0) {
                    skillCoolDown.put(skillID, coolDown - 1);
                } else {
                    if (coolDown == 0) {
                        MessageManager.ActionBarMessage(player, String.format("§c§l%s§r§e冷却完成", Skill.getInstance(skillID).skillName));
                        entry.setValue(-1);
                    }
                }

            }
        }, 0, 20);
    }

    public void clear() {
        skillCoolDown.replaceAll((k, v) -> 0);
    }

}

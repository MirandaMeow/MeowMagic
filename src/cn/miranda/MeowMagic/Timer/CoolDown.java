package cn.miranda.MeowMagic.Timer;

import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class CoolDown {
    private final HashMap<String, Integer> skillCoolDown;

    public CoolDown(HashMap<String, Integer> skillCoolDown) {
        this.skillCoolDown = skillCoolDown;
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            for (Map.Entry<String, Integer> entry : skillCoolDown.entrySet()) {
                String skillID = entry.getKey();
                int coolDown = entry.getValue();
                if (coolDown > 0) {
                    skillCoolDown.put(skillID, coolDown - 1);
                }
            }
        }, 0, 20);
    }

    public void clear() {
        skillCoolDown.replaceAll((k, v) -> 0);
    }

}

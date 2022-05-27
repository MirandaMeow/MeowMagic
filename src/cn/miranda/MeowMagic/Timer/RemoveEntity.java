package cn.miranda.MeowMagic.Timer;

import cn.miranda.MeowMagic.MeowMagic;
import cn.miranda.MeowMagic.Timer.Skill.ArrowShootTicker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

public class RemoveEntity {
    /**
     * 移除实体倒计时
     *
     * @param entity 将要被移除的实体
     * @param delay  延迟时间
     */
    public RemoveEntity(Entity entity, long delay) {
        BukkitTask task = Bukkit.getScheduler().runTaskLater(MeowMagic.plugin, () -> {
            ArrowShootTicker.arrowIDs.remove(String.valueOf(entity.getEntityId()));
            entity.remove();
        }, delay);
    }
}

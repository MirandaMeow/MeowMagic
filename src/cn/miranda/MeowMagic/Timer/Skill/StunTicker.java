package cn.miranda.MeowMagic.Timer.Skill;

import cn.miranda.MeowMagic.Lib.Effects;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

public class StunTicker {
    private BukkitTask task;
    private int duration;

    /**
     * 击昏效果定时器
     *
     * @param duration 持续时间
     * @param target   被击昏的目标
     */
    public StunTicker(int duration, LivingEntity target) {
        this.duration = duration;
        this.task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            target.setAI(false);
            if (this.duration > 0) {
                this.duration -= 1;
                Effects.StunEffect(target);
            } else {
                this.task.cancel();
                target.setAI(true);
            }
        }, 0L, 20L);
    }
}

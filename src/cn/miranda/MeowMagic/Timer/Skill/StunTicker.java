package cn.miranda.MeowMagic.Timer.Skill;

import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class StunTicker {
    private BukkitTask task;
    private final List<LivingEntity> monsters = new ArrayList<>();
    private int duration;

    /**
     * 击晕效果定时器
     *
     * @param duration 持续时间
     * @param entities 被击晕的生物
     */
    public StunTicker(int duration, List<Entity> entities) {
        this.duration = duration;
        task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity) entity;
                    monsters.add(livingEntity);
                    livingEntity.setAI(false);
                }
            }
            if (this.duration > 0) {
                this.duration -= 1;
            } else {
                task.cancel();
                for (LivingEntity livingEntity : monsters) {
                    livingEntity.setAI(true);
                }
                monsters.clear();
            }
        }, 0L, 20L);
    }
}

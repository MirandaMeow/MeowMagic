package cn.miranda.MeowMagic.Timer.Skill;

import cn.miranda.MeowMagic.Lib.Effects;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class HealTicker {
    private BukkitTask task;
    private int duration;

    /**
     * 治疗术效果定时器
     *
     * @param player   玩家
     * @param duration 持续时间
     * @param power    治疗量
     */
    public HealTicker(Player player, int duration, int power) {
        this.duration = duration;
        this.task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            if (this.duration > 0) {
                double playerCurrentHP = player.getHealth();
                double playerMaxHP = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                player.setHealth(Math.min(playerCurrentHP + (double) power / duration, playerMaxHP));
                this.duration -= 1;
                Effects.HealEffect(player);
            } else {
                this.task.cancel();
            }
        }, 0L, 20L);
    }
}

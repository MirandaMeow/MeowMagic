package cn.miranda.MeowMagic.Timer.Skill;

import cn.miranda.MeowMagic.Core.Notify;
import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class ShieldRestoreTicker {
    private final BukkitTask task;
    public int cooldown;
    public double shield;
    public int level;

    /**
     * 护盾冷却定时器
     *
     * @param cooldown 护盾冷却时间
     * @param power    护盾值
     * @param level    技能等级
     * @param player   玩家
     */
    public ShieldRestoreTicker(int cooldown, int power, int level, Player player) {
        this.cooldown = cooldown;
        this.shield = power;
        this.level = level;
        this.task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            if (this.cooldown > 0) {
                this.cooldown -= 1;
            } else {
                this.cooldown = cooldown;
                this.shield = power;
                MessageManager.Message(player, Notify.SHIELD_READY.string);
            }
        }, 0L, 20L);
    }

    /**
     * 扣除护盾值
     *
     * @param damage 受到的伤害
     * @return 护盾吸收的伤害
     */
    public double use(double damage) {
        double absorb = damage * 0.3;
        if (this.shield >= absorb) {
            this.shield -= absorb;
            return absorb;
        }
        absorb = this.shield;
        this.shield = 0;
        return absorb;
    }


    /**
     * 结束计时器
     */
    public void endTask() {
        this.task.cancel();
    }
}

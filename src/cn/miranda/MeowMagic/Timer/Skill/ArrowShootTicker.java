package cn.miranda.MeowMagic.Timer.Skill;

import cn.miranda.MeowMagic.MeowMagic;
import cn.miranda.MeowMagic.Timer.RemoveEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ArrowShootTicker {
    public static List<String> arrowIDs = new ArrayList<>();
    private BukkitTask task;
    private int duration;
    private final int power;

    /**
     * 箭雨效果定时器
     *
     * @param player   玩家
     * @param duration 波数
     * @param power    每波箭矢数
     */
    public ArrowShootTicker(Player player, int duration, int power) {
        this.duration = duration;
        this.power = power;
        this.task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            if (this.duration != 0) {
                this.duration -= 1;
                Vector z_axis = new Vector(0, 0, 1);
                Vector x_axis = new Vector(1, 0, 0);
                Vector y_axis = new Vector(0, 1, 0);
                for (int i = 0; i < this.power; i++) {
                    Vector a, b, loc = player.getEyeLocation().getDirection();
                    double range = 15 % 360;
                    double phi = (range / 180) * Math.PI;
                    Vector ax1 = loc.getCrossProduct(z_axis);
                    if (ax1.length() < 0.01) {
                        a = x_axis.clone();
                        b = y_axis.clone();
                    } else {
                        a = ax1.normalize();
                        b = loc.getCrossProduct(a).normalize();
                    }
                    double z = ThreadLocalRandom.current().nextDouble(Math.cos(phi), 1.0D);
                    double det = ThreadLocalRandom.current().nextDouble(0.0D, Math.PI * 2);
                    double theta = Math.acos(z);
                    Vector v = a.clone().multiply(Math.cos(det)).add(b.clone().multiply(Math.sin(det))).multiply(Math.sin(theta)).add(loc.clone().multiply(Math.cos(theta)));
                    Projectile projectile = player.launchProjectile(org.bukkit.entity.Arrow.class, v.normalize().multiply(2));
                    arrowIDs.add(String.valueOf(projectile.getEntityId()));
                    new RemoveEntity(projectile, 100L);
                }
            } else {
                this.task.cancel();
            }
        }, 0L, 6L);
    }
}

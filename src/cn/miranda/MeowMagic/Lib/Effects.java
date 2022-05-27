package cn.miranda.MeowMagic.Lib;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Effects {
    /**
     * 技能使用成功的效果
     *
     * @param player 玩家
     */
    public static void SkillSuccess(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation().add(0, 1, 0);
        world.spawnParticle(Particle.EXPLOSION_LARGE, location, 5, 0.3, 0.3, 0.3);
        world.playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
    }

    /**
     * 技能使用失败的效果
     *
     * @param player 玩家
     */
    public static void SkillFail(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation().add(0, 1, 0);
        world.spawnParticle(Particle.SMOKE_NORMAL, location, 15, 0.4, 0.4, 0.4);
        world.playSound(location, Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
    }

    /**
     * 治疗术的效果
     *
     * @param player 玩家
     */
    public static void HealEffect(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation().add(0, 1, 0);
        world.spawnParticle(Particle.HEART, location, 15, 0.4, 0.4, 0.4);
        world.playSound(location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
    }

    /**
     * 击昏的效果
     *
     * @param target 目标
     */
    public static void StunEffect(Entity target) {
        World world = target.getWorld();
        Location location = target.getLocation().add(0, 1, 0);
        world.spawnParticle(Particle.EXPLOSION_LARGE, location, 5, 0.3, 0, 0.3);
        world.playSound(location, Sound.ENTITY_TNT_PRIMED, 1, 1);
    }
}

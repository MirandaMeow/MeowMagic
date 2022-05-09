package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Timer.Skill.HealTicker;
import cn.miranda.MeowMagic.Timer.Skill.StunTicker;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;


public class SkillLib {

    /**
     * @param player   玩家
     * @param distance /
     * @param isRange  /
     * @param duration 持续时间
     * @param power    治疗量
     * @param entities /
     */
    public static void heal(Player player, int distance, boolean isRange, int duration, int power, List<Entity> entities) {
        new HealTicker(player, duration, power);
        User.getUser(player).setMaxMana(300);
    }

    /**
     * 击晕
     *
     * @param player   /
     * @param distance /
     * @param isRange  /
     * @param duration 持续时间
     * @param power    /
     * @param entities 被击晕的生物
     */
    public static void stun(Player player, int distance, boolean isRange, int duration, int power, List<Entity> entities) {
        new StunTicker(duration, entities);
    }
}
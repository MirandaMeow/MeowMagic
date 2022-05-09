package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Timer.Skill.HealTicker;
import org.bukkit.entity.Player;


public class SkillLib {
    /**
     * 治疗术
     *
     * @param player   玩家
     * @param distance 技能范围/距离
     * @param isRange  是否范围技能
     */
    public static void heal(Player player, int distance, boolean isRange, int duration, int power) {
        new HealTicker(player, duration, power);
    }
}
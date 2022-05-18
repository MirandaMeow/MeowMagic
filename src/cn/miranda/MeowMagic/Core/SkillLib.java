package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.Timer.Skill.HealTicker;
import cn.miranda.MeowMagic.Timer.Skill.StunTicker;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class SkillLib {

    /**
     * 治疗术
     *
     * @param player   玩家
     * @param distance /
     * @param isRange  /
     * @param duration 持续时间
     * @param power    治疗量
     * @param target   /
     * @return 技能是否使用成功
     */
    public static boolean heal(Player player, int distance, boolean isRange, int duration, int power, Entity target) {
        new HealTicker(player, duration, power);
        return true;
    }

    /**
     * 击晕
     *
     * @param player   /
     * @param distance /
     * @param isRange  /
     * @param duration 持续时间
     * @param power    /
     * @param target   释放对象
     * @return 技能是否使用成功
     */
    public static boolean stun(Player player, int distance, boolean isRange, int duration, int power, Entity target) {
        if (target instanceof LivingEntity) {
            new StunTicker(duration, (LivingEntity) target);
            return true;
        } else {
            MessageManager.Message(player, Notify.NOT_CREATURE.string);
            return false;
        }
    }
}
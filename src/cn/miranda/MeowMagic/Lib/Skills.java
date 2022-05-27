package cn.miranda.MeowMagic.Lib;

import cn.miranda.MeowMagic.Core.Notify;
import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Core.SkillPanel;
import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.Timer.Skill.ArrowShootTicker;
import cn.miranda.MeowMagic.Timer.Skill.HealTicker;
import cn.miranda.MeowMagic.Timer.Skill.ShieldRestoreTicker;
import cn.miranda.MeowMagic.Timer.Skill.StunTicker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class Skills {

    /**
     * 治疗术
     * 主动技能 interact
     *
     * @param player   使用治疗术的玩家
     * @param distance /
     * @param isRange  /
     * @param duration 持续时间
     * @param power    治疗量
     * @return 技能是否使用成功
     */
    public static boolean heal(Player player, int distance, boolean isRange, int duration, int power) {
        new HealTicker(player, duration, power);
        return true;
    }

    /**
     * 击昏
     * 主动技能 interactEntity
     *
     * @param player   使用击昏的玩家
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

    /**
     * 地质勘探
     * 主动技能 interact
     *
     * @param player   使用地质勘探的玩家
     * @param distance 勘探范围
     * @param isRange  /
     * @param duration /
     * @param power    /
     * @return 技能是否使用成功
     */
    public static boolean mineDetect(Player player, int distance, boolean isRange, int duration, int power) {
        Material material = player.getInventory().getItemInOffHand().getType();
        Material targetMaterial = null;
        switch (material) {
            case DIAMOND:
                targetMaterial = Material.DIAMOND_ORE;
                break;
            case EMERALD:
                targetMaterial = Material.EMERALD_ORE;
                break;
            case NETHERITE_INGOT:
                targetMaterial = Material.ANCIENT_DEBRIS;
                break;
        }
        Location playerLocation = player.getLocation();
        int playerX = (int) Math.round(playerLocation.getX());
        int playerY = (int) Math.round(playerLocation.getY());
        int playerZ = (int) Math.round(playerLocation.getZ());
        for (int currentRadius = 1; currentRadius <= distance; currentRadius++) {
            for (int x = -(2 * currentRadius - 1); x <= (2 * currentRadius - 1); x++) {
                for (int z = -(2 * currentRadius - 1); z <= (2 * currentRadius - 1); z++) {
                    int currentX = playerX + x;
                    int currentZ = playerZ + z;
                    Location currentLocation = new Location(player.getWorld(), currentX, playerY, currentZ);
                    if (currentLocation.getBlock().getType().equals(targetMaterial)) {
                        player.setCompassTarget(currentLocation);
                        MessageManager.Message(player, String.format(Notify.FOUND_ORE.string, (int) currentLocation.getX(), (int) currentLocation.getY(), (int) currentLocation.getZ()));
                        return true;
                    }
                }
            }
        }
        MessageManager.Message(player, Notify.NO_ORE.string);
        return true;
    }

    /**
     * 护盾
     * 被动技能
     *
     * @param player   触发护盾的玩家
     * @param cooldown 护盾充能冷却
     * @param power    护盾最大值
     * @param event    将要被处理的伤害事件
     * @return 技能是否使用成功
     */
    public static boolean shield(Player player, int cooldown, int power, EntityDamageByEntityEvent event) {
        User user = User.getUser(player);
        int level = user.skillState.getLevel("skill04");
        if (!Skill.shieldData.containsKey(player)) {
            Skill.shieldData.put(player, new ShieldRestoreTicker(cooldown, power, level, player));
        }
        if (Skill.shieldData.get(player).level != level) {
            Skill.shieldData.get(player).endTask();
            Skill.shieldData.put(player, new ShieldRestoreTicker(cooldown, power, level, player));
        }
        ShieldRestoreTicker shieldRestoreTicker = Skill.shieldData.get(player);
        double damage = event.getDamage();
        double absorb = shieldRestoreTicker.use(damage);
        double formatted = Double.parseDouble(String.format("%.2f", absorb));
        event.setDamage(damage - formatted);
        double shieldRemain = shieldRestoreTicker.shield;
        String formattedShieldRemain = String.format("%.2f", shieldRemain);
        if (shieldRemain > 0) {
            MessageManager.ActionBarMessage(player, String.format(Notify.SHIELD_ACTIVE.string, formatted, formattedShieldRemain));
        }
        Skill.getSkill("skill04").update(player, user.skillState);
        return true;
    }

    /**
     * 箭雨
     * 主动技能 interact
     *
     * @param player   使用箭雨的玩家
     * @param distance /
     * @param isRange  /
     * @param duration 箭雨的波数
     * @param power    每波箭雨的箭矢数量
     * @return 技能是否使用成功
     */
    public static boolean arrow(Player player, int distance, boolean isRange, int duration, int power) {
        new ArrowShootTicker(player, duration, power);
        return true;
    }
}
package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.Timer.Skill.HealTicker;
import cn.miranda.MeowMagic.Timer.Skill.StunTicker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;


public class SkillLib {

    /**
     * 治疗术
     *
     * @param player   使用治疗术的玩家
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
     * 击昏
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
     *
     * @param player   使用地质勘探的玩家
     * @param distance 勘探范围
     * @param isRange  /
     * @param duration /
     * @param power    /
     * @param target   /
     * @return 技能是否使用成功
     */
    public static boolean mineDetect(Player player, int distance, boolean isRange, int duration, int power, Entity target) {
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
        MessageManager.HoverMessage(player, "地质勘探", Skill.getInstance("skill03").getDescription(player));
        return true;
    }
}
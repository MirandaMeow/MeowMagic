package cn.miranda.MeowMagic.SkillLib;

import org.bukkit.entity.Player;


public class Skills {
    public static void heal(Player player, int cost, int coolDown, int distance, boolean isRange, float chance) {
        player.sendMessage("触发成功");
    }
}
package cn.miranda.MeowMagic.Core;

public enum Notify {
    NO_MANA("§e无法发动§c§l%s§r§e  需要§b%d§e点魔法"),
    COOL_DOWN("§c§l%s§r§e还未冷却  剩余§b%d§e秒"),
    NEED_OFF_HAND("§e发动§c§l%s§r§e副手需要§b%s*%d"),
    FAILED("§c§l%s§r§e使用失败"),
    ACTIVE("§c§l%s§r§e发动"),
    NOT_CREATURE("§c目标不是生物"),
    FOUND_ORE("§e当前层范围内离你最近的钻石矿位于 §b(%d %d %d)"),
    NO_ORE("§e当前层范围内没有发现指定矿物"),
    COOL_DOWN_CLEAR("§e所有技能冷却完成"),
    SHIELD_READY("§e护盾已经充能完毕"),
    SHIELD_ACTIVE("§e护盾吸收了 §b%s §e伤害  护盾剩余 §b%s");

    public final String string;

    /**
     * @param string 提示文本
     */
    Notify(String string) {
        this.string = string;
    }
}

package cn.miranda.MeowMagic.Core;

public enum Notify {
    NO_MANA("§e无法发动§c§l%s§r§e  需要§b%d§e点魔法"),
    COOL_DOWN("§c§l%s§r§e还未冷却  剩余§b%d§e秒"),
    NEED_OFF_HAND("§e发动§c§l%s§r§e副手需要§b%s*%d"),
    FAILED("§c§l%s§r§e使用失败"),
    ACTIVE("§c§l%s§r§e发动"),
    NOT_CREATURE("§c目标不是生物");

    public String string;

    /**
     * @param string 提示文本
     */
    Notify(String string) {
        this.string = string;
    }
}

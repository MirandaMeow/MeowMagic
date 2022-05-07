package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.ConfigManager;
import org.bukkit.entity.Player;

import static cn.miranda.MeowMagic.Manager.ConfigManager.playerRegain;

public class User {
    private final Player player;
    private int maxMana;
    private int gainMana;
    private final String playerName;

    /**
     * 初始化玩家
     *
     * @param player 玩家对象
     */
    public User(Player player) {
        this.playerName = player.getName();
        this.player = player;
        this.load();
    }

    /**
     * 使玩家每秒恢复一定的魔法
     */
    public void gainMana() {
        int mana = this.player.getLevel();
        if (mana != this.maxMana) {
            this.player.setLevel(mana + this.gainMana);
            this.player.setExp((float) this.player.getLevel() / this.maxMana);
        }
    }

    /**
     * 检查玩家的魔法值是否足够
     *
     * @param check 检查值
     * @return 玩家的魔法值是否足够
     */
    public boolean manaCheck(int check) {
        return this.player.getLevel() >= check;
    }

    /**
     * 扣除玩家的魔法值
     *
     * @param reduce 扣除的魔法值
     */
    public void reduceMana(int reduce) {
        int mana = this.player.getLevel();
        this.player.setLevel(mana - reduce);
        this.player.setExp((float) this.player.getLevel() / this.maxMana);
    }

    /**
     * 保存玩家数据
     */
    private void save() {
        playerRegain.set(String.format("%s.maxMana", this.playerName), 100);
        playerRegain.set(String.format("%s.gainMana", this.playerName), 1);
        ConfigManager.saveConfig(playerRegain);
    }

    /**
     * 载入玩家数据
     */
    private void load() {
        if (playerRegain.getString(this.playerName) == null) {
            this.maxMana = 100;
            this.gainMana = 1;
            playerRegain.set(String.format("%s.maxMana", this.playerName), 100);
            playerRegain.set(String.format("%s.gainMana", this.playerName), 1);
            this.save();
            return;
        }
        int maxMana = playerRegain.getInt(String.format("%s.maxMana", this.playerName));
        int gainMana = playerRegain.getInt(String.format("%s.gainMana", this.playerName));
        this.maxMana = maxMana;
        this.gainMana = gainMana;
    }
}

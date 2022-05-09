package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.ConfigManager;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.entity.Player;

import java.util.HashMap;

import static cn.miranda.MeowMagic.Manager.ConfigManager.players;

public class User {
    private final Player player;
    private int maxMana;
    private int gainMana;
    private final String playerName;
    public SkillState skillState;

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
        if (mana < this.maxMana) {
            this.player.setLevel(mana + this.gainMana);
            this.player.setExp((float) this.player.getLevel() / this.maxMana);
        } else {
            this.player.setLevel(this.maxMana);
            this.player.setExp(1);
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
        players.set(String.format("%s.maxMana", this.playerName), this.maxMana);
        players.set(String.format("%s.gainMana", this.playerName), this.gainMana);
        players.set(String.format("%s.skills", this.playerName), this.skillState.skillList);
        ConfigManager.saveConfig(players);
    }

    /**
     * 载入玩家数据
     */
    private void load() {
        if (players.getString(this.playerName) == null) {
            this.maxMana = 100;
            this.gainMana = 1;
            players.set(String.format("%s.maxMana", this.playerName), this.maxMana);
            players.set(String.format("%s.gainMana", this.playerName), this.gainMana);
            this.skillState = new SkillState(player);
            this.save();
            return;
        }
        this.maxMana = players.getInt(String.format("%s.maxMana", this.playerName));
        this.gainMana = players.getInt(String.format("%s.gainMana", this.playerName));
        this.skillState = new SkillState(player);
        this.skillState.addSkill("skill01");
    }

    public static User getUser(Player player) {
        return MeowMagic.users.get(player);
    }
}

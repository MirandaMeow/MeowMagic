package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.ConfigManager;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.entity.Player;

import static cn.miranda.MeowMagic.Manager.ConfigManager.players;

public class User {
    private final Player player;
    private int maxMana;
    private int reGainMana;
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
     *
     * @param addMana 增加的魔法
     */
    public void reGainMana(int addMana) {
        int add;
        if (addMana == -1) {
            add = this.reGainMana;
        } else {
            add = addMana;
        }
        this.addMana(add);
    }

    /**
     * 刷新玩家的经验条
     */
    public void refreshExpBar() {
        this.player.setExp((float) this.player.getLevel() / this.maxMana);
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
        this.refreshExpBar();
    }

    /**
     * 增加玩家的魔法值
     *
     * @param mana 增加的魔法值
     */
    public void addMana(int mana) {
        int currentMana = this.player.getLevel();
        this.player.setLevel(Math.min(currentMana + mana, this.maxMana));
        this.refreshExpBar();
    }

    /**
     * 保存玩家数据
     */
    private void save() {
        players.set(String.format("%s.maxMana", this.playerName), this.maxMana);
        players.set(String.format("%s.gainMana", this.playerName), this.reGainMana);
        ConfigManager.saveConfig(players);
    }

    /**
     * 获取技能等级
     *
     * @param skillID 技能 ID
     * @return 技能等级
     */
    public int getLevel(String skillID) {
        return players.getInt(String.format("%s.skills.%s.level", this.playerName, skillID));
    }

    /**
     * 获取技能升级所需经验
     *
     * @param skillID 技能 ID
     * @return 技能升级所需经验
     */
    public int getSkillMaxExp(String skillID) {
        return players.getInt(String.format("%s.skills.%s.maxExp", this.playerName, skillID));
    }

    /**
     * 获取技能当前经验
     *
     * @param skillID 技能 ID
     * @return 技能当前经验
     */
    public int getSkillCurrentExp(String skillID) {
        return players.getInt(String.format("%s.skills.%s.currentExp", this.playerName, skillID));
    }

    /**
     * 获取每秒恢复魔法值
     *
     * @return 每秒恢复魔法值
     */
    public int getReGainMana() {
        return this.reGainMana;
    }

    /**
     * 获取最大魔法值
     *
     * @return 最大魔法值
     */
    public int getMaxMana() {
        return this.maxMana;
    }

    /**
     * 设置每秒恢复魔法
     *
     * @param reGainMana 每秒恢复魔法
     */
    public void setReGainMana(int reGainMana) {
        this.reGainMana = reGainMana;
        this.save();
    }

    /**
     * 设置最大魔法
     *
     * @param maxMana 最大魔法
     */
    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
        this.refreshExpBar();
        this.save();
    }

    /**
     * 载入玩家数据
     */
    private void load() {
        if (players.getString(this.playerName) == null) {
            this.maxMana = 100;
            this.reGainMana = 1;
            players.set(String.format("%s.maxMana", this.playerName), this.maxMana);
            players.set(String.format("%s.gainMana", this.playerName), this.reGainMana);
            this.skillState = new SkillState(player);
            this.save();
            return;
        }
        this.maxMana = players.getInt(String.format("%s.maxMana", this.playerName));
        this.reGainMana = players.getInt(String.format("%s.gainMana", this.playerName));
        this.skillState = new SkillState(player);
    }

    /**
     * 获取 User 实例
     *
     * @param player 玩家
     * @return User 实例
     */
    public static User getUser(Player player) {
        return MeowMagic.users.get(player);
    }
}

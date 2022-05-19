package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.ConfigManager;
import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.Timer.CoolDown;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import static cn.miranda.MeowMagic.Manager.ConfigManager.players;

public class SkillState {
    private final Player player;
    public HashMap<String, Integer> skillLevel = new HashMap<>();
    public final HashMap<String, Integer> skillCoolDown = new HashMap<>();
    private final CoolDown coolDownTimer;

    /**
     * 初始化玩家技能状态实例
     *
     * @param player 玩家
     */
    public SkillState(Player player) {
        this.player = player;
        ConfigurationSection skillConfig = players.getConfigurationSection(String.format("%s.skills", this.player.getName()));
        try {
            assert skillConfig != null;
            for (String skillID : skillConfig.getValues(false).keySet()) {
                this.skillLevel.put(skillID, skillConfig.getInt(String.format("%s.level", skillID)));
            }
        } catch (NullPointerException e) {
            players.set(String.format("%s.skills", this.player.getName()), this.skillLevel);
            this.save();
        }
        for (Map.Entry<String, Integer> entry : this.skillLevel.entrySet()) {
            this.skillCoolDown.put(entry.getKey(), -1);
        }
        this.coolDownTimer = new CoolDown(this.skillCoolDown, this.player);
    }

    /**
     * 给玩家增加一个技能
     *
     * @param skillID 技能 ID
     */
    public void addSkill(String skillID) {
        this.skillLevel.put(skillID, 0);
        this.skillCoolDown.put(skillID, 0);
        players.set(String.format("%s.skills.%s.maxExp", player.getName(), skillID), Skill.getSkill(skillID).exp.get(0));
        players.set(String.format("%s.skills.%s.currentExp", player.getName(), skillID), 0);
        players.set(String.format("%s.skills.%s.level", player.getName(), skillID), 0);
        this.save();
    }

    /**
     * 保存技能状态实例
     */
    public void save() {
        ConfigManager.saveConfig(players);
    }

    /**
     * 清除玩家所有技能的冷却时间
     */
    public void clearCoolDown() {
        this.coolDownTimer.clear();
        MessageManager.Message(player, Notify.COOL_DOWN_CLEAR.string);
    }


    /**
     * 技能使用失败时让其进入冷却
     *
     * @param skillID 技能 ID
     */
    public void doCoolDown(String skillID) {
        this.skillCoolDown.put(skillID, Skill.getSkill(skillID).coolDown.get(this.skillLevel.get(skillID)));
    }

    /**
     * 查询玩家是否持有技能
     *
     * @param skillID 技能 ID
     * @return 返回玩家是否持有技能
     */
    public boolean hasSkill(String skillID) {
        return this.skillLevel.containsKey(skillID);
    }

    /**
     * 检查技能是否处于冷却中
     *
     * @param skillID 技能 ID
     * @return 返回技能是否处于冷却
     */
    public int checkCoolDown(String skillID) {
        return this.skillCoolDown.get(skillID);
    }
}

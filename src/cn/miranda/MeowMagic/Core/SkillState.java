package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.ConfigManager;
import cn.miranda.MeowMagic.Timer.CoolDown;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static cn.miranda.MeowMagic.Manager.ConfigManager.players;

public class SkillState {
    private final Player player;
    public HashMap<String, Integer> skillList = new HashMap<>();
    private final HashMap<String, Integer> skillCoolDown = new HashMap<>();
    private final CoolDown coolDownTimer;

    public SkillState(Player player) {
        this.player = player;
        ConfigurationSection skillConfig = players.getConfigurationSection(String.format("%s.skills", this.player.getName()));
        try {
            assert skillConfig != null;
            for (String skillID : skillConfig.getValues(false).keySet()) {
                this.skillList.put(skillID, skillConfig.getInt(skillID));
            }
        } catch (NullPointerException e) {
            players.set(String.format("%s.skills", this.player.getName()), this.skillList);
            this.save();
        }
        for (Map.Entry<String, Integer> entry : this.skillList.entrySet()) {
            this.skillCoolDown.put(entry.getKey(), 0);
        }
        this.coolDownTimer = new CoolDown(this.skillCoolDown);
    }

    public void addSkill(String skillID) {
        this.skillList.put(skillID, 0);
        this.skillCoolDown.put(skillID, 0);
        this.save();
    }

    private void save() {
        players.set(String.format("%s.skills", this.player.getName()), this.skillList);
        ConfigManager.saveConfig(players);
    }

    public void clearCoolDown() {
        this.coolDownTimer.clear();
    }

    public void fireSkill(String skillID) {
        if (!this.skillList.containsKey(skillID)) {
            return;
        }
        try {
            Skill.getInstance(skillID).fire(this.player, this.skillList.get(skillID));
        } catch (InvocationTargetException | IllegalAccessException ignored) {
        }
        this.skillCoolDown.put(skillID, Skill.getInstance(skillID).coolDown.get(this.skillList.get(skillID)));
    }

    public boolean hasSkill(String skillID) {
        return this.skillList.containsKey(skillID);
    }

    public int checkCoolDown(String skillID) {
        return this.skillCoolDown.get(skillID);
    }
}

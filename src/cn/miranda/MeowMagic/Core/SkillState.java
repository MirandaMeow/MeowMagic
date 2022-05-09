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
    private final ConfigurationSection playerConfig;
    private final HashMap<String, Integer> skillCoolDown = new HashMap<>();
    private final CoolDown coolDownTimer;

    public SkillState(Player player) {
        this.player = player;
        this.playerConfig = players.getConfigurationSection(player.getName());
        assert playerConfig != null;
        for (String skillID : playerConfig.getValues(false).keySet()) {
            skillList.put(skillID, playerConfig.getInt(String.format("skills.%s", skillID)));
        }
        for (Map.Entry<String, Integer> entry : skillList.entrySet()) {
            skillCoolDown.put(entry.getKey(), 0);
        }
        this.coolDownTimer = new CoolDown(skillCoolDown);
    }

    public void addSkill(String skillID) {
        this.skillList.put(skillID, 0);
        this.skillCoolDown.put(skillID, 0);
        this.save();
    }

    private void save() {
        this.playerConfig.set("skills", this.skillList);
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
    }

    public boolean hasSkill(String skillID) {
        return this.skillList.containsKey(skillID);
    }
}

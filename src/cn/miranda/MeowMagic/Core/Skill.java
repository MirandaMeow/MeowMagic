package cn.miranda.MeowMagic.Core;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.miranda.MeowMagic.Manager.ConfigManager.players;
import static cn.miranda.MeowMagic.Manager.ConfigManager.skills;

public class Skill {
    public static HashMap<String, Skill> skillMap = new HashMap<>();
    public String skillID;
    public String skillName;
    public List<String> description;
    public List<Integer> duration;
    public final List<Integer> cost;
    public final List<Integer> coolDown;
    private final List<Integer> distance;
    private final boolean isRange;
    public final List<Integer> chance;
    private Method skill;
    public List<Material> itemList = new ArrayList<>();
    public final List<Action> click = new ArrayList<>();
    public boolean sneak;
    public final List<Integer> exp;
    private final List<Integer> power;

    /**
     * 获取技能实例（单例模式）
     *
     * @param skillID 技能 ID
     * @return 返回技能实例
     */
    public static Skill getInstance(String skillID) {
        if (skillMap.containsKey(skillID)) {
            return skillMap.get(skillID);
        } else {
            Skill skill = new Skill(skillID);
            skillMap.put(skillID, skill);
            return skill;
        }
    }

    /**
     * 初始化技能
     *
     * @param skillID 技能 ID
     */
    public Skill(String skillID) {
        ConfigurationSection skill = skills.getConfigurationSection(skillID);
        assert skill != null;
        this.skillID = skill.getString("id");
        this.skillName = skill.getString("name");
        this.description = (List<String>) skill.getList("description");
        this.duration = (List<Integer>) skill.getList("duration");
        this.cost = (List<Integer>) skill.getList("cost");
        this.coolDown = (List<Integer>) skill.getList("cooldown");
        this.distance = (List<Integer>) skill.getList("distance");
        this.isRange = skill.getBoolean("isRange");
        this.chance = (List<Integer>) skill.getList("chance");
        String skillInternalID = skill.getString("skill");
        assert skillInternalID != null;
        try {
            this.skill = SkillLib.class.getDeclaredMethod(skillInternalID, Player.class, int.class, boolean.class, int.class, int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        List<String> items = skill.getStringList("item");
        for (String item : items) {
            itemList.add(Material.getMaterial(item));
        }
        String clickString = skill.getString("click");
        if (Objects.equals(clickString, "right")) {
            click.add(Action.RIGHT_CLICK_AIR);
            click.add(Action.RIGHT_CLICK_BLOCK);
        } else {
            click.add(Action.LEFT_CLICK_AIR);
            click.add(Action.LEFT_CLICK_BLOCK);
        }
        this.sneak = skill.getBoolean("sneak");
        this.exp = (List<Integer>) skill.getList("exp");
        this.power = (List<Integer>) skill.getList("power");
    }


    /**
     * 触发技能
     *
     * @param player 触发技能的玩家
     * @param level  技能等级
     * @throws InvocationTargetException 报错
     * @throws IllegalAccessException    报错
     */
    public void activate(Player player, int level) throws InvocationTargetException, IllegalAccessException {
        int distance = this.distance.get(level);
        int duration = this.duration.get(level);
        int power = this.power.get(level);
        this.skill.invoke(null, player, distance, this.isRange, duration, power);
    }

    /**
     * 获取技能说明
     *
     * @param player 指定玩家
     * @return 返回技能说明文本列表
     */
    public List<String> getDescription(Player player) {
        List<String> description = new ArrayList<>();
        description.add("§c§l" + this.skillName);
        int currentExp = players.getInt(String.format("%s.skills.%s.currentExp", player.getName(), this.skillID));
        int maxExp = players.getInt(String.format("%s.skills.%s.maxExp", player.getName(), this.skillID));
        int nowLevel = players.getInt(String.format("%s.skills.%s.level", player.getName(), this.skillID));
        description.add(String.format("§e等级: §b%d §e经验值 §b%d/%d", nowLevel + 1, currentExp, maxExp));
        description.add("");
        for (String line : this.description) {
            description.add(this.replace(line, nowLevel));
        }
        return description;
    }

    /**
     * 替换占位符动态显示数据
     *
     * @param line  传入的技能描述文本行
     * @param level 技能等级
     * @return 处理后的技能描述文本行
     */
    private String replace(String line, int level) {
        String pattern = ".*?%(.+?)%.*";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        if (matcher.find()) {
            switch (matcher.group(1)) {
                case "power":
                    return line.replace("%power%", String.valueOf(this.power.get(level)));
                case "duration":
                    return line.replace("%duration%", this.duration.get(level).toString());
                case "cost":
                    return line.replace("%cost%", this.cost.get(level).toString());
                case "cooldown":
                    return line.replace("%cooldown%", this.coolDown.get(level).toString());
                case "chance":
                    return line.replace("%chance%", String.valueOf(this.chance.get(level)));
            }
        } else {
            return line;
        }
        return line;
    }

    /**
     * 使得技能经验值提示，满足条件时升级
     *
     * @param skillID    技能 ID
     * @param player     触发技能的玩家
     * @param skillState 玩家的技能状态实例
     */
    public void update(String skillID, Player player, SkillState skillState) {
        Skill skill = Skill.getInstance(skillID);
        int current = players.getInt(String.format("%s.skills.%s.currentExp", player.getName(), skillID));
        int max = players.getInt(String.format("%s.skills.%s.maxExp", player.getName(), skillID));
        int level = players.getInt(String.format("%s.skills.%s.level", player.getName(), skillID));
        int newMaxExp;
        current += 1;
        if (current == max) {
            if (level != 4) {
                level += 1;
                newMaxExp = skill.exp.get(level);
            } else {
                newMaxExp = 0;
            }
            players.set(String.format("%s.skills.%s.maxExp", player.getName(), skillID), newMaxExp);
            players.set(String.format("%s.skills.%s.currentExp", player.getName(), skillID), 0);
            players.set(String.format("%s.skills.%s.level", player.getName(), skillID), level);
            skillState.skillLevel.put(skillID, level);
        } else {
            if (level == 4) {
                return;
            }
            players.set(String.format("%s.skills.%s.currentExp", player.getName(), skillID), current);
        }
        skillState.save();
    }

    /**
     * 在插件启用时初始化所有技能
     */
    public static void loadAllSkills() {
        Skill.getInstance("skill01");
    }
}

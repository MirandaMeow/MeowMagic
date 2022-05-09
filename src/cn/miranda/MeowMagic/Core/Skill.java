package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.SkillLib.Skills;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.miranda.MeowMagic.Manager.ConfigManager.skills;

public class Skill {
    public static HashMap<String, Skill> skillMap = new HashMap<>();
    public String skillID;
    public String skillName;
    public List<String> description;
    public List<Integer> duration;
    public final List<Integer> cost;
    private final List<Integer> coolDown;
    private final List<Integer> distance;
    private final boolean isRange;
    private final List<Float> chance;
    private Method skill;
    public List<Material> itemList = new ArrayList<>();
    public final List<Action> click = new ArrayList<>();
    public boolean sneak;
    private final List<Integer> update;
    private final List<Integer> power;

    public static Skill getInstance(String skillID) {
        if (skillMap.containsKey(skillID)) {
            return skillMap.get(skillID);
        } else {
            Skill skill = new Skill(skillID);
            skillMap.put(skillID, skill);
            return skill;
        }
    }

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
        this.chance = skill.getFloatList("chance");
        String skillInternalID = skill.getString("skill");
        assert skillInternalID != null;
        try {
            this.skill = Skills.class.getDeclaredMethod(skillInternalID, Player.class, int.class, int.class, int.class, boolean.class, float.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        List<String> items = skill.getStringList("item");
        for (String item : items) {
            item = item.toUpperCase();
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
        this.update = (List<Integer>) skill.getList("update");
        this.power = (List<Integer>) skill.getList("power");
    }


    public void fire(Player player, int level) throws InvocationTargetException, IllegalAccessException {
        int cost = this.cost.get(level);
        int coolDown = this.coolDown.get(level);
        int distance = this.distance.get(level);
        float chance = this.chance.get(level);
        this.skill.invoke(null, player, cost, coolDown, distance, this.isRange, chance);
    }

    public List<String> getDescription(int level) {
        List<String> description = new ArrayList<>();
        description.add("§c§l" + this.skillName);
        for (String line : this.description) {
            description.add(this.replace(line, level));
        }
        return description;
    }

    private String replace(String line, int level) {
        String placeholder = null;
        String pattern = "%(.+)%";
        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(line);
        if (matcher.find()) {
            switch (matcher.group(1)) {
                case "power":
                    return line.replace("%power%", this.power.get(level).toString());
                case "duration":
                    return line.replace("%duration%", this.duration.get(level).toString());
                case "cost":
                    return line.replace("%cost%", this.cost.get(level).toString());
                case "cooldown":
                    return line.replace("%cooldown%", this.cost.get(level).toString());
            }
        } else {
            return line;
        }
        return line;
    }

    public static void loadAllSkills() {
        Skill.getInstance("skill01");
    }
}

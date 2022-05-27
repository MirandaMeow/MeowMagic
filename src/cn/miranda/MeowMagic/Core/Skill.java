package cn.miranda.MeowMagic.Core;

import cn.miranda.MeowMagic.Event.PlayerGainSkillExp;
import cn.miranda.MeowMagic.Lib.Skills;
import cn.miranda.MeowMagic.Timer.Skill.ShieldRestoreTicker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.miranda.MeowMagic.Manager.ConfigManager.players;
import static cn.miranda.MeowMagic.Manager.ConfigManager.skills;

public class Skill {
    public static final HashMap<String, Skill> skillMap = new HashMap<>();
    public static final HashMap<Player, ShieldRestoreTicker> shieldData = new HashMap<>();
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
    public final List<Material> mainHand = new ArrayList<>();
    public final List<Action> click = new ArrayList<>();
    public boolean sneak;
    public final List<Integer> exp;
    private final List<Integer> power;
    public String invokeType;
    public final List<Material> offhand = new ArrayList<>();
    public String offHandItemName;
    public int offhandCost;
    public boolean isPositive;
    public ItemStack icon;

    /**
     * 获取技能实例（单例模式）
     *
     * @param skillID 技能 ID
     * @return 返回技能实例
     */
    public static Skill getSkill(String skillID) {
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
        this.description = skill.getStringList("description");
        this.duration = skill.getIntegerList("duration");
        this.cost = skill.getIntegerList("cost");
        this.coolDown = skill.getIntegerList("cooldown");
        this.distance = skill.getIntegerList("distance");
        this.isRange = skill.getBoolean("isRange");
        this.chance = skill.getIntegerList("chance");
        this.invokeType = skill.getString("invokeType");
        String skillInvoker = skill.getString("skill");
        assert skillInvoker != null;
        try {
            switch (this.invokeType) {
                case "interact":
                    this.skill = Skills.class.getDeclaredMethod(skillInvoker, Player.class, int.class, boolean.class, int.class, int.class);
                    break;
                case "interactEntity":
                    this.skill = Skills.class.getDeclaredMethod(skillInvoker, Player.class, int.class, boolean.class, int.class, int.class, Entity.class);
                    break;
                case "playerHitByOther":
                    this.skill = Skills.class.getDeclaredMethod(skillInvoker, Player.class, int.class, int.class, EntityDamageByEntityEvent.class);
                    break;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        List<String> items = skill.getStringList("mainhand");
        for (String current : items) {
            this.mainHand.add(Material.getMaterial(current, false));
        }
        String clickString = skill.getString("click");
        if (Objects.equals(clickString, "right")) {
            this.click.add(Action.RIGHT_CLICK_AIR);
            this.click.add(Action.RIGHT_CLICK_BLOCK);
        } else {
            this.click.add(Action.LEFT_CLICK_AIR);
            this.click.add(Action.LEFT_CLICK_BLOCK);
        }
        this.sneak = skill.getBoolean("sneak");
        this.exp = skill.getIntegerList("exp");
        this.power = skill.getIntegerList("power");
        List<String> offhand_list = skill.getStringList("offhand");
        for (String current : offhand_list) {
            this.offhand.add(Material.getMaterial(current, false));
        }
        this.offHandItemName = skill.getString("offHandItemName");
        this.offhandCost = skill.getInt("offhandCost");
        this.isPositive = skill.getBoolean("isPositive");
        String icon = skill.getString("icon");
        assert icon != null;
        Material iconMaterial = Material.getMaterial(icon);
        assert iconMaterial != null;
        this.icon = new ItemStack(iconMaterial, 1);
    }


    /**
     * 交互触发技能
     *
     * @param player 触发技能的玩家
     * @param level  技能等级
     * @return 是否触发成功
     */
    public boolean interact(Player player, int level) {
        int distance = this.distance.get(level);
        int duration = this.duration.get(level);
        int power = this.power.get(level);
        try {
            return (boolean) this.skill.invoke(null, player, distance, this.isRange, duration, power);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 交互实体触发技能
     *
     * @param player 触发技能的玩家
     * @param target 被技能影响的实体
     * @param level  技能等级
     * @return 是否触发成功
     */
    public boolean interactEntity(Player player, Entity target, int level) {
        int distance = this.distance.get(level);
        int duration = this.duration.get(level);
        int power = this.power.get(level);
        try {
            return (boolean) this.skill.invoke(null, player, distance, this.isRange, duration, power, target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param player 触发事件的玩家
     * @param event  触发的事件
     * @param level  技能等级
     */
    public void playerHitByOther(Player player, EntityDamageByEntityEvent event, int level) {
        int cooldown = this.coolDown.get(level);
        int power = this.power.get(level);
        try {
            this.skill.invoke(null, player, cooldown, power, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取技能说明
     *
     * @param player 指定玩家
     * @return 返回技能说明文本列表
     */
    public List<String> getDescription(Player player, int level) {
        List<String> description = new ArrayList<>();
        description.add("§c§l" + this.skillName);
        if (level != -1) {
            int currentExp = players.getInt(String.format("%s.skills.%s.currentExp", player.getName(), this.skillID));
            int maxExp = players.getInt(String.format("%s.skills.%s.maxExp", player.getName(), this.skillID));
            int nowLevel = players.getInt(String.format("%s.skills.%s.level", player.getName(), this.skillID));
            description.add(String.format("§e等级: §b%d §e经验值 §b%d§e/§b%d", nowLevel + 1, currentExp, maxExp));
            description.add("");
        }
        for (String line : this.description) {
            description.add(this.replace(line, level));
        }
        return description;
    }

    /**
     * 将列表转化为字符串
     *
     * @param list 将要被转换的列表
     * @return 转化后的字符串
     */
    private String listToString(List<Integer> list, boolean isPercentage, int level) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            int current = list.get(i);
            if (i == level) {
                builder.append("§9");
            } else {
                builder.append("§b");
            }
            builder.append(current);
            if (isPercentage) {
                builder.append("%§e/");
            } else {
                builder.append("§e/");
            }
        }
        String out = builder.toString();
        return out.substring(0, out.length() - 3);
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
                    return line.replace("%power%", this.listToString(this.power, false, level));
                case "duration":
                    return line.replace("%duration%", this.listToString(this.duration, false, level));
                case "cost":
                    return line.replace("%cost%", this.listToString(this.cost, false, level));
                case "cooldown":
                    return line.replace("%cooldown%", this.listToString(this.coolDown, false, level));
                case "chance":
                    line = line.substring(0, line.length() - 1);
                    return line.replace("%chance%", this.listToString(this.chance, true, level));
                case "distance":
                    return line.replace("%distance%", this.listToString(this.distance, false, level));
                case "exp":
                    return line.replace("%exp%", this.listToString(this.exp, false, level));
            }
        } else {
            return line;
        }
        return line;
    }

    /**
     * 使得技能经验值提示，满足条件时升级
     *
     * @param player     触发技能的玩家
     * @param skillState 玩家的技能状态实例
     */
    public void update(Player player, SkillState skillState) {
        Skill skill = Skill.getSkill(this.skillID);
        int current = players.getInt(String.format("%s.skills.%s.currentExp", player.getName(), this.skillID));
        int max = players.getInt(String.format("%s.skills.%s.maxExp", player.getName(), this.skillID));
        int level = players.getInt(String.format("%s.skills.%s.level", player.getName(), this.skillID));
        int newMaxExp;
        current += 1;
        if (current == max) {
            if (level != 4) {
                level += 1;
                newMaxExp = skill.exp.get(level);
            } else {
                newMaxExp = 0;
            }
            players.set(String.format("%s.skills.%s.maxExp", player.getName(), this.skillID), newMaxExp);
            players.set(String.format("%s.skills.%s.currentExp", player.getName(), this.skillID), 0);
            players.set(String.format("%s.skills.%s.level", player.getName(), this.skillID), level);
            skillState.skillLevel.put(this.skillID, level);
        } else {
            if (level == 4) {
                return;
            }
            players.set(String.format("%s.skills.%s.currentExp", player.getName(), this.skillID), current);
        }
        Bukkit.getPluginManager().callEvent(new PlayerGainSkillExp(player));
        skillState.save();
    }

    /**
     * 在插件启用时初始化所有技能
     */
    public static void loadAllSkills() {
        Set<String> skills_list = skills.getValues(false).keySet();
        for (String skill : skills_list) {
            Skill.getSkill(skill);
        }
    }

    /**
     * 以技能名获取技能 ID
     *
     * @param skillName 技能名
     * @return 技能 ID
     */
    public static String getSkillIDByName(String skillName) {
        for (Skill skill : skillMap.values()) {
            if (skill.skillName.equals(skillName)) {
                return skill.skillID;
            }
        }
        return null;
    }
}

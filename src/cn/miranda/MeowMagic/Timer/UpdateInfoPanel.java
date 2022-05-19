package cn.miranda.MeowMagic.Timer;

import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

import static cn.miranda.MeowMagic.Manager.ConfigManager.players;

public class UpdateInfoPanel {
    private final BukkitTask task;

    public UpdateInfoPanel(Player player, String skillID) {
        this.task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            Skill skill = Skill.getSkill(skillID);
            Scoreboard scoreboard;
            Objective objective;
            if (player.getScoreboard().getObjective("SkillPanel") == null) {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                objective = scoreboard.registerNewObjective("SkillPanel", "dummy", "§c§l" + skill.skillName, RenderType.INTEGER);
            } else {
                scoreboard = player.getScoreboard();
                objective = scoreboard.getObjective("SkillPanel");
                assert objective != null;
                objective.setDisplayName("§c§l" + skill.skillName);
            }
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            int level = players.getInt(String.format("%s.skills.%s.level", player.getName(), skillID)) + 1;
            int max = players.getInt(String.format("%s.skills.%s.maxExp", player.getName(), skillID));
            int current = players.getInt(String.format("%s.skills.%s.currentExp", player.getName(), skillID));
            int cooldown = User.getUser(player).skillState.checkCoolDown(skillID);
            if (cooldown == -1) {
                cooldown = 0;
            }
            objective.getScore("§a当前等级").setScore(level);
            objective.getScore("§a升级经验值").setScore(max);
            objective.getScore("§a当前经验值").setScore(current);
            objective.getScore("§a冷却时间").setScore(cooldown);
            player.setScoreboard(scoreboard);
        }, 0L, 5L);
    }

    public void stopTick() {
        this.task.cancel();
    }
}

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

public class UpdateInfoPanel {
    private final BukkitTask task;

    /**
     * 更新玩家右侧栏定时器
     *
     * @param player  玩家
     * @param skillID 技能 ID
     */
    public UpdateInfoPanel(Player player, String skillID) {
        this.task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            Skill skill = Skill.getSkill(skillID);
            Scoreboard scoreboard;
            Objective objective;
            if (player.getScoreboard().getObjective("SkillPanel") == null) {
                scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                objective = scoreboard.registerNewObjective("SkillPanel", "dummy", "§9§l" + skill.skillName, RenderType.INTEGER);
            } else {
                scoreboard = player.getScoreboard();
                objective = scoreboard.getObjective("SkillPanel");
                assert objective != null;
                objective.setDisplayName("§9§l" + skill.skillName);
            }
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            int cooldown = User.getUser(player).skillState.checkCoolDown(skillID);
            if (cooldown == -1) {
                cooldown = 0;
            }
            User user = User.getUser(player);
            objective.getScore("§a当前等级").setScore(user.getLevel(skillID));
            objective.getScore("§a升级经验值").setScore(user.getSkillMaxExp(skillID));
            objective.getScore("§a当前经验值").setScore(user.getSkillCurrentExp(skillID));
            objective.getScore("§a冷却时间").setScore(cooldown);
            player.setScoreboard(scoreboard);
        }, 0L, 5L);
    }

    public void stopTick() {
        this.task.cancel();
    }
}

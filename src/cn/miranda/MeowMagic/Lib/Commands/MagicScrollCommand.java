package cn.miranda.MeowMagic.Lib.Commands;

import cn.miranda.MeowMagic.Core.MagicScroll;
import cn.miranda.MeowMagic.Core.Notify;
import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Lib.Skills;
import cn.miranda.MeowMagic.Manager.CommandManager;
import cn.miranda.MeowMagic.Manager.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MagicScrollCommand implements TabExecutor {
    /**
     * 触发命令
     *
     * @param commandSender 使用命令的人
     * @param command       命令
     * @param s             ？
     * @param strings       命令参数
     * @return
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!CommandManager.check(false, command, commandSender)) {
            return true;
        }
        if (strings.length != 2) {
            MessageManager.Message(commandSender, Notify.COMMAND_LENGTH_ERROR.string);
            return true;
        }
        Player target = Bukkit.getPlayer(strings[0]);
        String skillName = strings[1];
        if (target == null) {
            MessageManager.Message(commandSender, Notify.NO_PLAYER_ERROR.string);
            return true;
        }
        if (!Skills.getAllSkillName().contains(skillName)) {
            MessageManager.Message(commandSender, Notify.SKILL_NOT_FOUND.string);
            return true;
        }
        new MagicScroll(Skill.getSkillIDByName(skillName)).toPlayer(target);
        return true;
    }

    /**
     * 补全命令参数
     *
     * @param commandSender 使用命令的人
     * @param command       命令
     * @param s             ？
     * @param strings       命令参数
     * @return 被补全的命令参数
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }
            return CommandManager.filterTabList(players, strings[0]);
        }
        if (strings.length == 2) {
            return CommandManager.filterTabList(Skills.getAllSkillName(), strings[1]);
        }
        return new ArrayList<>();
    }
}

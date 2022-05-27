package cn.miranda.MeowMagic.Lib.Commands;

import cn.miranda.MeowMagic.Core.Notify;
import cn.miranda.MeowMagic.Manager.CommandManager;
import cn.miranda.MeowMagic.Manager.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SkillPanelCommand implements TabExecutor {
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
        if (!CommandManager.check(true, command, commandSender)) {
            return true;
        }
        Player player = (Player) commandSender;
        Player target;
        if (strings.length == 0) {
            target = player;
        } else {
            if (strings.length != 1) {
                MessageManager.Message(player, Notify.COMMAND_LENGTH_ERROR.string);
                return true;
            } else {
                target = this.getPlayer(strings[0]);
            }
        }
        if (target == null) {
            MessageManager.Message(player, Notify.NO_PLAYER_ERROR.string);
            return true;
        }
        new cn.miranda.MeowMagic.Core.SkillPanel(target).openPanel();
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
        return new ArrayList<>();
    }

    private Player getPlayer(String playerName) {
        return Bukkit.getPlayer(playerName);
    }
}

package cn.miranda.MeowMagic.Manager;

import cn.miranda.MeowMagic.Core.Notify;
import cn.miranda.MeowMagic.Lib.Commands.MagicScrollCommand;
import cn.miranda.MeowMagic.Lib.Commands.SkillPanelCommand;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.*;

public class CommandManager {
    private static HashMap<String, TabExecutor> commandMap = new HashMap<>();

    /**
     * 注册命令
     *
     * @param command  命令
     * @param executor 执行器
     */
    private static void register(String command, TabExecutor executor) {
        PluginCommand pluginCommand = MeowMagic.plugin.getCommand(command);
        assert pluginCommand != null;
        pluginCommand.setExecutor(executor);
    }

    /**
     * 注册所有命令
     */
    public static void registerAll() {
        commandMap.put("skills", new SkillPanelCommand());
        commandMap.put("magicscroll", new MagicScrollCommand());
        for (Map.Entry<String, TabExecutor> entry : commandMap.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 检测命令能否被执行
     *
     * @param checkConsole 是否需要检测触发者为控制台
     * @param command      命令
     * @param sender       执行命令的对象
     * @return
     */
    public static boolean check(boolean checkConsole, Command command, CommandSender sender) {
        if (checkConsole) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Notify.NO_CONSOLE.string);
                return false;
            }
        }
        if (!(sender.hasPermission(Objects.requireNonNull(command.getPermission())))) {
            MessageManager.Message(sender, Notify.NO_PERMISSION.string);
            return false;
        }
        return true;
    }

    /**
     * 根据输入的字符串来过滤备选列表
     *
     * @param list  将要被过滤的列表
     * @param input 输入的字符
     * @return 过滤后的列表
     */
    public static List<String> filterTabList(List<String> list, String input) {
        List<String> selected = new ArrayList<>();
        for (String s : list) {
            if (s.contains(input)) {
                selected.add(s);
            }
        }
        if (selected.size() != 0) {
            return selected;
        }
        return list;
    }
}

package cn.miranda.MeowMagic.Manager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MessageManager {
    /**
     * 向后台发送信息
     *
     * @param message 需要发送的信息
     */
    public static void Console(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    /**
     * 在服务器广播消息
     *
     * @param message 需要广播的消息
     */
    public static void Broadcast(String message) {
        Bukkit.getServer().broadcastMessage(message);
    }

    /**
     * 向玩家发送消息
     *
     * @param player  接收信息的玩家
     * @param message 消息内容
     */
    public static void Message(Player player, String message) {
        player.sendMessage(message);
    }

    /**
     * 向玩家发送 ActionBar 消息
     *
     * @param player  接收信息的玩家
     * @param message 消息内容
     */
    public static void ActionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    /**
     * 向玩家发送带有悬浮信息的消息
     *
     * @param player 接收信息的玩家
     * @param title  消息内容
     * @param lines  悬浮信息内容
     */
    public static void HoverMessage(Player player, String title, List<String> lines) {
        TextComponent message = new TextComponent(title);
        List<BaseComponent> list = new ArrayList<>();
        for (String line : lines) {
            list.add(new TextComponent(line));
            if (lines.indexOf(line) != lines.size() - 1) {
                list.add(new TextComponent("\n"));
            }
        }
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, list.toArray(new BaseComponent[0])));
        player.spigot().sendMessage(message);
    }
}

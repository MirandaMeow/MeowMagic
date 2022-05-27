package cn.miranda.MeowMagic.Event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerGainSkillExp extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;

    /**
     * 返回事件是否取消
     *
     * @return 事件是否取消
     */
    @Override
    public boolean isCancelled() {
        return false;
    }

    /**
     * 设置事件是否取消
     *
     * @param b 事件取消
     */
    @Override
    public void setCancelled(boolean b) {
    }

    /**
     * 获取事件列表
     *
     * @return 获取事件列表
     */
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    /**
     * 获取事件列表
     *
     * @return 获取事件列表
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * 构造事件函数
     *
     * @param player 触发事件的玩家
     */
    public PlayerGainSkillExp(Player player) {
        this.player = player;
    }

    /**
     * 获取玩家
     *
     * @return 玩家
     */
    public Player getPlayer() {
        return this.player;
    }
}

package cn.miranda.MeowMagic.Listener;

import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLoginLogoutEvent implements Listener {
    /**
     * 当玩家登录时使该玩家自动回复魔法
     *
     * @param event 玩家登录事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void PlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MessageManager.Message(player, "登录");
        if (!MeowMagic.users.containsKey(player)) {
            MeowMagic.users.put(player, new User(player));
        }
    }

    /**
     * 当玩家登出时从处理列表钟去除该玩家
     *
     * @param event 玩家登出事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void PlayerLogout(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        MessageManager.Message(player, "登出");
        MeowMagic.users.remove(player);
    }
}

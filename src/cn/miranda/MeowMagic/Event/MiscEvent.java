package cn.miranda.MeowMagic.Event;

import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MiscEvent implements Listener {
    /**
     * 当玩家登录时使该玩家自动回复魔法
     *
     * @param event 玩家登录事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void PlayerLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MeowMagic.users.put(player, new User(player));
    }

    /**
     * 禁用怪物掉落经验球
     *
     * @param event 实体死亡事件
     */
    @EventHandler(priority = EventPriority.NORMAL)
    private void BanDropExp(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            return;
        }
        event.setDroppedExp(0);
    }
}

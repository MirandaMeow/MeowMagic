package cn.miranda.MeowMagic.Events;

import cn.miranda.MeowMagic.Core.Skill;
import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;

public class BanDropExpEvent implements Listener {
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

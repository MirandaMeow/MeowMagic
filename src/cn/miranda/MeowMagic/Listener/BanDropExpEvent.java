package cn.miranda.MeowMagic.Listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class BanDropExpEvent implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    private void BanDropExp(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            return;
        }
        event.setDroppedExp(0);
    }
}
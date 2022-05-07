package cn.miranda.MeowMagic.Timer;


import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class AutoRegainMana {
    private static AutoRegainMana instance;
    private boolean on;
    private BukkitTask task;

    /**
     * 初始化自动恢复时钟
     */
    private AutoRegainMana() {
        this.on = false;
    }

    /**
     * 启用时钟
     */
    public void setOn() {
        if (this.on) {
            return;
        }
        this.on = true;
        task = Bukkit.getScheduler().runTaskTimer(MeowMagic.plugin, () -> {
            if (MeowMagic.users.isEmpty()) {
                return;
            }
            for (Map.Entry<Player, User> entry : MeowMagic.users.entrySet()) {
                User user = entry.getValue();
                user.gainMana();
            }
        }, 0L, 20L);
    }

    /**
     * 禁用时钟
     */
    public void setOff() {
        if (!this.on) {
            return;
        }
        this.on = false;
        task.cancel();
    }


    /**
     * @return 获取时钟实例
     */
    public static AutoRegainMana getInstance() {
        if (instance == null) {
            instance = new AutoRegainMana();
        }
        return instance;
    }
}

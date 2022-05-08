package cn.miranda.MeowMagic;

import cn.miranda.MeowMagic.Core.User;
import cn.miranda.MeowMagic.Manager.ConfigManager;
import cn.miranda.MeowMagic.Manager.ListenerManager;
import cn.miranda.MeowMagic.Manager.MessageManager;
import cn.miranda.MeowMagic.Timer.AutoRegainMana;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class MeowMagic extends JavaPlugin {
    public static MeowMagic plugin;
    public static String version;
    public static AutoRegainMana regain;
    public static HashMap<Player, User> users = new HashMap<>();

    /**
     * 插件初始化
     */
    public MeowMagic() {
        plugin = this;
        version = this.getDescription().getVersion();
    }

    /**
     * 插件启用
     */
    public void onEnable() {
        MessageManager.Console("[猫子魔法] 启动");
        ListenerManager.registerAll();
        regain = AutoRegainMana.getInstance();
        regain.setOn();
        ConfigManager.loadConfigs();
    }

    /**
     * 插件禁用
     */
    public void onDisable() {
        MessageManager.Console("[猫子魔法] 禁用");
        regain.setOff();
        ConfigManager.saveConfigs();
    }
}

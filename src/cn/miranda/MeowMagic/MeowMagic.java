package cn.miranda.MeowMagic;

import cn.miranda.MeowMagic.Core.Skill;
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
    public String version;
    public static AutoRegainMana regain;
    public static final HashMap<Player, User> users = new HashMap<>();

    /**
     * 插件初始化
     */
    public MeowMagic() {
        plugin = this;
        this.version = this.getDescription().getVersion();
    }

    /**
     * 插件启用
     */
    public void onEnable() {
        MessageManager.Console(String.format("[猫子魔法] 启动 版本 %s", this.version));
        ListenerManager.registerAll();
        regain = AutoRegainMana.getInstance();
        regain.setOn();
        ConfigManager.loadConfigs();
        Skill.loadAllSkills();
    }

    /**
     * 插件禁用
     */
    public void onDisable() {
        MessageManager.Console(String.format("[猫子魔法] 禁用 版本 %s", this.version));
        regain.setOff();
        ConfigManager.saveConfigs();
    }
}

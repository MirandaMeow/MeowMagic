package cn.miranda.MeowMagic.Manager;

import cn.miranda.MeowMagic.Event.MiscEvent;
import cn.miranda.MeowMagic.Event.SkillInvokerEvent;
import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {
    private static final List<Listener> list = new ArrayList<>();

    /**
     * 向插件管理器注册事件
     *
     * @param listener 将要被注册的事件
     */
    private static void register(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, MeowMagic.plugin);
    }

    /**
     * 注册插件的所有事件
     */
    public static void registerAll() {
        list.add(new MiscEvent());
        list.add(new SkillInvokerEvent());
        for (Listener listener : list) {
            register(listener);
        }
    }
}

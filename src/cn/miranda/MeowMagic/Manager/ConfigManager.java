package cn.miranda.MeowMagic.Manager;

import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    public static YamlConfiguration players;
    public static YamlConfiguration skills;
    private static final HashMap<YamlConfiguration, File> configs = new HashMap<>();

    /**
     * 载入配置文件
     *
     * @param fileName 配置文件名
     * @return yaml 配置文件对象
     */
    private static YamlConfiguration load(String fileName) {
        File configFile = new File(MeowMagic.plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            MeowMagic.plugin.saveResource(fileName, false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(config, configFile);
        return config;
    }

    /**
     * 载入所有配置文件
     */
    public static void loadConfigs() {
        players = load("players.yml");
        skills = load("skills.yml");
    }

    /**
     * 保存所有配置文件
     */
    public static void saveConfigs() {
        try {
            for (Map.Entry<YamlConfiguration, File> current : configs.entrySet()) {
                YamlConfiguration currentYaml = current.getKey();
                File currentFile = current.getValue();
                currentYaml.save(currentFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存指定配置
     *
     * @param yamlConfiguration 将要被保存的配置
     */
    public static void saveConfig(YamlConfiguration yamlConfiguration) {
        File file = configs.get(yamlConfiguration);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

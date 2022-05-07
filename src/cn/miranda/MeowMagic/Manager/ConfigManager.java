package cn.miranda.MeowMagic.Manager;

import cn.miranda.MeowMagic.MeowMagic;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    public static YamlConfiguration playerGainData;
    private static final HashMap<YamlConfiguration, File> configs = new HashMap<>();

    private static YamlConfiguration load(String fileName) {
        File configFile = new File(MeowMagic.plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            MeowMagic.plugin.saveResource(fileName, false);
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        configs.put(config, configFile);
        return config;
    }

    public static void loadConfigs() {
        playerGainData = load("players.yml");
    }

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

    public static void saveConfig(YamlConfiguration yamlConfiguration) {
        File file = configs.get(yamlConfiguration);
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

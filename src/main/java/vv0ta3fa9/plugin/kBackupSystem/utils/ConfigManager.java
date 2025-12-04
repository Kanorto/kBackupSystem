package vv0ta3fa9.plugin.kParticleTools.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import vv0ta3fa9.plugin.kParticleTools.kBackSystem;
import vv0ta3fa9.plugin.kParticleTools.utils.Color.Colorizer;
import vv0ta3fa9.plugin.kParticleTools.utils.Color.impl.LegacyAdvancedColorizer;
import vv0ta3fa9.plugin.kParticleTools.utils.Color.impl.LegacyColorizer;
import vv0ta3fa9.plugin.kParticleTools.utils.Color.impl.MiniMessageColorizer;
import vv0ta3fa9.plugin.kParticleTools.utils.Color.impl.VanillaColorizer;
import vv0ta3fa9.plugin.kParticleTools.utils.Runner.Runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    private final kBackSystem plugin;
    protected FileConfiguration config;
    private File configFile;
    public Colorizer COLORIZER;
    private Runner runner;

    public ConfigManager(kBackSystem plugin, Runner runner) {
        this.plugin = plugin;
        this.runner = runner;
        loadConfigFiles();
    }

    private void loadConfigFiles() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                plugin.saveResource("config.yml", false);
            } catch (Exception e) {
                plugin.getLogger().warning("Не удалось сохранить config.yml: " + e.getMessage());
                plugin.getLogger().warning("Создайте папку plugins/kMobWaves/ вручную и дайте права на запись");
            }
        }
        if (configFile.exists()) {
            config = YamlConfiguration.loadConfiguration(configFile);
        }
    }
    public boolean getBoolean(String path, boolean def) {
        if (config == null) return def;
        return config.contains(path) ? config.getBoolean(path) : def;
    }

    public String getString(String path, String def) {
        if (config == null) return def;
        return config.contains(path) ? config.getString(path) : def;
    }

    public List<String> getStringList(String path) {
        if (config == null) return new ArrayList<>();
        return config.getStringList(path);
    }

    public boolean getDebug() {
        return getBoolean("debug", true);
    }

    public void setupColorizer() {
        COLORIZER = switch (getString("serializer", "LEGACY").toUpperCase()) {
            case "MINIMESSAGE" -> new MiniMessageColorizer();
            case "LEGACY" -> new LegacyColorizer(plugin);
            case "LEGACY_ADVANCED" -> new LegacyAdvancedColorizer(plugin);
            default -> new VanillaColorizer(plugin);
        };
    }
}


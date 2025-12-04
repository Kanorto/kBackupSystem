package vv0ta3fa9.plugin.kBackupSystem;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import vv0ta3fa9.plugin.kBackupSystem.utils.BackupManager;
import vv0ta3fa9.plugin.kBackupSystem.main.CommandManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.Color.Colorizer;
import vv0ta3fa9.plugin.kBackupSystem.utils.Color.impl.LegacyColorizer;
import vv0ta3fa9.plugin.kBackupSystem.utils.TaskManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.ConfigManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.Utils;

public final class kBackupSystem extends JavaPlugin {

    private Colorizer colorizer;
    protected ConfigManager configManager;
    protected Utils utils;
    protected CommandManager commandsManager;
    protected KBSManager kbsManager;
    protected BackupManager backupManager;
    protected TaskManager taskManager;
    public boolean reload = false;

    PluginManager pluginManager;



    @Override
    public void onEnable() {
        try {
            kbsManager = new KBSManager(this);
            colorizer = new LegacyColorizer(this);
            kbsManager.loadingConfiguration();
            kbsManager.loadClassses();
            kbsManager.registerCommands();
            if (configManager.getBackupinStart() || !reload) {
                getLogger().info("| Плагин загружен. Начат бэкап миров...");
                backupManager.createBackupsAllWorlds();
            }
            if (configManager.gettask()) {
                kbsManager.startTask();
                getLogger().info("| Запуск задач...");
            }
            if (configManager.deleteolds()) {
                backupManager.deleteOldBackups();
            }
        } catch (Exception e) {
            getLogger().severe("ОШИБКА ВКЛЮЧЕНИЯ ПЛАГИНА! Выключение плагина...");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (configManager.getBackupinStop()) {
            getLogger().info("| ⚠ Сервер завершается. Запуск бэкапа миров...");
            backupManager.createBackupsAllWorlds();
        }
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Utils getUtils() {
        return utils;
    }

    public BackupManager getBackupManager() {
        return backupManager;
    }
    public Colorizer getColorizer() {
        return colorizer;
    }


}

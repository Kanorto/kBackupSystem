package vv0ta3fa9.plugin.kBackupSystem;

import vv0ta3fa9.plugin.kBackupSystem.utils.BackupManager;
import vv0ta3fa9.plugin.kBackupSystem.main.CommandManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.TaskManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.ConfigManager;
import vv0ta3fa9.plugin.kBackupSystem.utils.Utils;

public class KBSManager {

    private final kBackupSystem plugin;

    public KBSManager(kBackupSystem plugin) {
        this.plugin = plugin;
    }



    protected void loadClassses() {
        plugin.taskManager = new TaskManager(plugin);
        plugin.backupManager = new BackupManager(plugin);
    }

    protected void startTask() {
        int intervalMinutes = plugin.getConfigManager().time();
        long intervalTicks = intervalMinutes * 60L * 20L;

        plugin.getServer().getScheduler().runTaskTimerAsynchronously(
                plugin,                                   // плагин-владелец задачи
                () -> plugin.getBackupManager().createBackupsAllWorlds(), // задача
                intervalTicks,                            // задержка перед первым запуском
                intervalTicks                             // интервал между запусками
        );

        plugin.getLogger().info("| [BackupScheduler] Задача начата: "
                + intervalMinutes + " min.");
    }

    protected void loadingConfiguration() {
        try {
            plugin.configManager = new ConfigManager(plugin);
            plugin.utils = new Utils();
            plugin.commandsManager = new CommandManager(plugin);
        } catch (Exception e) {
            plugin.getLogger().severe("ОШИБКА ЗАГРУЗКИ КОНФИГУРАЦИИ! Выключение плагина...");
            e.printStackTrace();
            throw e;
        }
    }
    protected void registerCommands() {
        if (plugin.getCommand("kbackupsystem") != null) plugin.getCommand("kbackupsystem").setExecutor(plugin.commandsManager);
        else plugin.getLogger().severe("Команда 'kmobwaves' не найдена в plugin.yml!");
    }


}

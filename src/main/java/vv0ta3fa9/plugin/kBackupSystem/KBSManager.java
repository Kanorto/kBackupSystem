package vv0ta3fa9.plugin.kBackupSystem;



public final class KBSManager extends kBackupSystem{

    @Override
    public void onEnable() {
        try {
            kbsManager.loadingConfiguration();
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



}

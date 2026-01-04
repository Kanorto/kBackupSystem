package vv0ta3fa9.plugin.kBackupSystem.utils;

import org.bukkit.Bukkit;
import vv0ta3fa9.plugin.kBackupSystem.kBackupSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupManager {

    private final kBackupSystem plugin;
    private File backupDir;

    public BackupManager(kBackupSystem plugin) {
        this.plugin = plugin;

        boolean debug = plugin.getConfigManager().getDebug();

        try {
            plugin.getLogger().info(".------------");
            plugin.getLogger().info("| Настройка директории бэкапов...");

            if (plugin.getConfigManager().getBackupType().equals("main")) {
                backupDir = new File(plugin.getDataFolder().getParentFile().getParentFile(), "backups");
                plugin.getLogger().info("| Используется корневая директория /backups/");
            } else {
                backupDir = new File(plugin.getDataFolder(), "backups");
                plugin.getLogger().info("| Используется локальная директория плагина /backups/");
            }

            if (!backupDir.exists()) {
                if (backupDir.mkdirs()) {
                    plugin.getLogger().info("| ✅ Папка создана: " + backupDir.getPath());
                } else {
                    plugin.getLogger().warning("| ❌ Не удалось создать папку: " + backupDir.getPath());
                }
            }

            if (debug) plugin.getLogger().info("| [DEBUG] backupDir = " + backupDir.getAbsolutePath());

        } catch (Exception e) {
            plugin.getLogger().severe("| ❌ Ошибка инициализации BackupManager: " + e.getMessage());
        }
    }

    public void createBackupsAllWorlds() {
        List<String> worlds = plugin.getConfigManager().getWordlList();
        boolean debug = plugin.getConfigManager().getDebug();

        if (worlds == null || worlds.isEmpty()) {
            plugin.getLogger().warning("| ❌ Список миров пуст или не найден в конфиге!");
            return;
        }

        plugin.getLogger().info("| Запуск последовательного бэкапа миров...");

        if (debug) plugin.getLogger().info("| [DEBUG] Миров в списке: " + worlds.size());

        backupNextWorld(worlds, 0);
    }

    private void backupNextWorld(List<String> worlds, int index) {
        boolean debug = plugin.getConfigManager().getDebug();

        if (index >= worlds.size()) {
            plugin.getLogger().info("| ✅ Все бэкапы миров завершены!");
            return;
        }

        String worldName = worlds.get(index);

        plugin.getLogger().info("| -----------------------------");
        plugin.getLogger().info("| Начат бэкап мира: " + worldName);

        if (debug) plugin.getLogger().info("| [DEBUG] Индекс мира: " + index);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            createBackupWorlds(worldName);

            if (debug) plugin.getLogger().info("| [DEBUG] Мир " + worldName + " успешно обработан");

            // Переход к следующему миру с задержкой
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                backupNextWorld(worlds, index + 1);
            }, 40L); // Задержка между бэкапами: 2 секунды
        });
    }

    public void createBackupWorlds(String worldName) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {

            long startTime = System.currentTimeMillis();
            boolean debug = plugin.getConfigManager().getDebug();

            try {
                if (debug) plugin.getLogger().info("| [DEBUG] Запуск создания бэкапа мира: " + worldName);

                File worldDir = Bukkit.getWorld(worldName).getWorldFolder();

                if (!worldDir.exists()) {
                    plugin.getLogger().warning("| ❌ Папка мира не найдена: " + worldDir);
                    return;
                }

                if (debug) plugin.getLogger().info("| [DEBUG] Папка мира: " + worldDir.getAbsolutePath());

                File archive = createZipArchive(worldDir, worldName);

                if (archive == null) {
                    plugin.getLogger().warning("| ❌ Ошибка при создании архива");
                    return;
                }

                long duration = (System.currentTimeMillis() - startTime);
                plugin.getLogger().info("| ✅ Бэкап создан: " + archive.getName() + " (" + duration + " ms)");

            } catch (Exception e) {
                plugin.getLogger().severe("| ❌ Ошибка при создании бэкапа: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }



    private File createZipArchive(File sourceDir, String worldName) {
        boolean debug = plugin.getConfigManager().getDebug();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = sdf.format(new Date());
            String archiveName = worldName + "_" + timestamp + ".zip";

            if (!backupDir.exists()) backupDir.mkdirs();
            File archive = new File(backupDir, archiveName);

            if (debug) plugin.getLogger().info("| [DEBUG] Создание ZIP архива: " + archive.getAbsolutePath());

            try (FileOutputStream fos = new FileOutputStream(archive);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                String rootEntryName = sourceDir.getName() + "/";
                zipDirectory(sourceDir, rootEntryName, zos, debug);

                zos.finish();
            }

            return archive;

        } catch (Exception e) {
            plugin.getLogger().severe("| ❌ Ошибка создания архива: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Рекурсивно добавляет файл/папку в ZipOutputStream.
     *
     * @param file      текущий файл или директория
     * @param entryName относительный путь в архиве (должен заканчиваться '/' для директорий)
     * @param zos       ZipOutputStream
     * @param debug     флаг детального логирования
     * @throws IOException
     */
    private void zipDirectory(File file, String entryName, ZipOutputStream zos, boolean debug) throws IOException {

        if (file.isDirectory()) {
            ZipEntry dirEntry = new ZipEntry(entryName);
            zos.putNextEntry(dirEntry);
            zos.closeEntry();

            File[] children = file.listFiles();
            if (children == null) return;

            if (debug) {
                plugin.getLogger().info("| [DEBUG] Папка: " + file.getAbsolutePath() + " (" + children.length + " файлов)");
            }

            for (File child : children) {
                String childEntryName = entryName + child.getName() + (child.isDirectory() ? "/" : "");
                zipDirectory(child, childEntryName, zos, debug);
            }
            return;
        }
        if (debug) plugin.getLogger().info("| [DEBUG] Файл: " + entryName + " (размер=" + file.length() + " байт)");

        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[16384];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
        }

        zos.closeEntry();
    }
    public void deleteOldBackups() {
        if (backupDir == null || !backupDir.exists()) {
            plugin.getLogger().warning("| ❌ Папка backups не найдена. Очистка невозможна.");
            return;
        }

        int days = plugin.getConfigManager().getdaystodelete();

        boolean debug = plugin.getConfigManager().getDebug();
        long now = System.currentTimeMillis();
        long maxAge = days * 24L * 60L * 60L * 1000L;

        File[] files = backupDir.listFiles((dir, name) -> name.endsWith(".zip"));

        if (files == null || files.length == 0) {
            plugin.getLogger().info("| Нет архивов для проверки.");
            return;
        }

        plugin.getLogger().info("| -----------------------------");
        plugin.getLogger().info("| Очистка бэкапов старше " + days + " дней...");

        for (File f : files) {
            long age = now - f.lastModified();

            if (debug) {
                plugin.getLogger().info("| [DEBUG] Проверка: " + f.getName() +
                        " | Возраст: " + (age / 1000 / 60 / 60 / 24) + " дней");
            }

            if (age > maxAge) {
                if (f.delete()) {
                    plugin.getLogger().info("| 🗑 Удалён старый бэкап: " + f.getName());
                } else {
                    plugin.getLogger().warning("| ❌ Не удалось удалить: " + f.getName());
                }
            }
        }

        plugin.getLogger().info("| Очистка завершена.");
    }

}


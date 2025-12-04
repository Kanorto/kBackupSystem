package vv0ta3fa9.plugin.kParticleTools;

import org.bukkit.plugin.java.JavaPlugin;
import vv0ta3fa9.plugin.kParticleTools.commands.CommandManager;
import vv0ta3fa9.plugin.kParticleTools.utils.ConfigManager;
import vv0ta3fa9.plugin.kParticleTools.utils.DataManager;
import vv0ta3fa9.plugin.kParticleTools.utils.MessagesManager;
import vv0ta3fa9.plugin.kParticleTools.utils.Runner.PaperRunner;
import vv0ta3fa9.plugin.kParticleTools.utils.Runner.Runner;
import vv0ta3fa9.plugin.kParticleTools.utils.Utils;

public final class kBackSystem extends JavaPlugin {

    private ConfigManager configManager;
    private MessagesManager messagesManager;
    private Utils utils;
    private final Runner runner = new PaperRunner(this);
    private CommandManager commandsManager;
    private DataManager dataManager;


    @Override
    public void onEnable() {
        try {
            loadingConfiguration();
            configManager.setupColorizer();
            registerCommands();

        } catch (Exception e) {
            getLogger().severe("ОШИБКА ВКЛЮЧЕНИЯ ПЛАГИНА! Выключение плагина...");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
    }

    private void loadingConfiguration() {
        try {
            configManager = new ConfigManager(this, runner);
            utils = new Utils();
            messagesManager = new MessagesManager(this, runner);
            commandsManager = new CommandManager(this, runner);
            dataManager = new DataManager(this, runner);
        } catch (Exception e) {
            getLogger().severe("ОШИБКА ЗАГРУЗКИ КОНФИГУРАЦИИ! Выключение плагина...");
            e.printStackTrace();
            throw e;
        }
    }
    private void registerCommands() {
        if (getCommand("kparticletools") != null) getCommand("kparticletools").setExecutor(commandsManager);
        else getLogger().severe("Команда 'kmobwaves' не найдена в plugin.yml!");
    }

    // ---- Геттеры ----

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Utils getUtils() {
        return utils;
    }

    public DataManager getDataManager() { return dataManager; }

}

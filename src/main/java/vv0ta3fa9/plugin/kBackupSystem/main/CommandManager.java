package vv0ta3fa9.plugin.kBackupSystem.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import vv0ta3fa9.plugin.kBackupSystem.kBackupSystem;
import vv0ta3fa9.plugin.kBackupSystem.utils.Runner.Runner;

public class CommandManager implements CommandExecutor {

    private final kBackupSystem plugin;
    private Runner runner;

    public CommandManager(kBackupSystem plugin, Runner runner) {
        this.plugin = plugin;
        this.runner = runner;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            send(sender, "§cИспользование: /kmobwaves <reload|start|stop|info|force_start>");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        return false;
    }

    private void send(CommandSender sender, String msg) {
        sender.sendMessage(plugin.getConfigManager().COLORIZER.colorize(msg));
    }
}

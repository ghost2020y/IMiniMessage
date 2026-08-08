package me.zortex.iminimessage.command;

import me.zortex.iminimessage.IMiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;

public class IMiniMessageCommand implements CommandExecutor {

    private final IMiniMessage plugin;

    public IMiniMessageCommand(IMiniMessage plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!sender.hasPermission("iminimessage.use")) {
            plugin.getLangManager().sendMessage(sender, "no-permission");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("iminimessage.reload")) {
                plugin.getLangManager().sendMessage(sender, "no-permission");
                return true;
            }

            plugin.reloadPluginConfigurations();
            plugin.getLangManager().sendMessage(sender, "reload-success");
            return true;
        }

        plugin.getLangManager().sendMessage(sender, "help-message");
        return true;
    }
}
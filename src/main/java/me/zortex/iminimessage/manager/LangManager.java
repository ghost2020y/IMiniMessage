package me.zortex.iminimessage.manager;

import me.zortex.iminimessage.IMiniMessage;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class LangManager {

    private final IMiniMessage plugin;
    private final MiniMessage miniMessage;
    private FileConfiguration langConfig;

    public LangManager(IMiniMessage plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        reload();
    }

    public void reload() {
        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        InputStream defConfigStream = plugin.getResource("lang.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)
            );
            langConfig.setDefaults(defConfig);
        }
    }

    public void sendMessage(CommandSender sender, String key) {
        String message = langConfig.getString(key);
        if (message != null && !message.isEmpty()) {
            sender.sendMessage(miniMessage.deserialize(message));
        }
    }
}
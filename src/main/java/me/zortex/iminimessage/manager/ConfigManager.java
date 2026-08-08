package me.zortex.iminimessage.manager;

import me.zortex.iminimessage.IMiniMessage;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final IMiniMessage plugin;
    private volatile boolean convertLegacy;
    private volatile boolean parseOnlyWithTags;

    public ConfigManager(IMiniMessage plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        this.convertLegacy = config.getBoolean("convert-legacy", true);
        this.parseOnlyWithTags = config.getBoolean("parse-only-with-tags", true);
    }

    public boolean isConvertLegacy() {
        return convertLegacy;
    }

    public boolean isParseOnlyWithTags() {
        return parseOnlyWithTags;
    }
}
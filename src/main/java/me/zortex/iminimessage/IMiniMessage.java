package me.zortex.iminimessage;

import com.github.retrooper.packetevents.PacketEvents;
import me.zortex.iminimessage.converter.LegacyConverter;
import me.zortex.iminimessage.listener.ChatListener;
import me.zortex.iminimessage.listener.PacketListener;
import me.zortex.iminimessage.manager.ConfigManager;
import me.zortex.iminimessage.processor.ComponentProcessor;
import me.zortex.iminimessage.utils.VersionUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class IMiniMessage extends JavaPlugin {

    private static IMiniMessage instance;
    private ConfigManager configManager;
    private PacketListener packetListener;

    @Override
    public void onEnable() {
        instance = this;

        if (!VersionUtils.isPaper()) {
            getLogger().severe("====================================================");
            getLogger().severe("ERROR: Unsupported core detected!");
            getLogger().severe("The IMiniMessage plugin is NOT supported on pure Spigot.");
            getLogger().severe("Please use Paper or its forks (Purpur, Folia, etc.).");
            getLogger().severe("====================================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!VersionUtils.isSupportedVersion(1, 19, 4)) {
            getLogger().severe("====================================================");
            getLogger().severe("ERROR: Unsupported Minecraft version!");
            getLogger().severe("IMiniMessage supports versions 1.19.4 and later.");
            getLogger().severe("Current server version: " + getServer().getBukkitVersion());
            getLogger().severe("====================================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!getServer().getPluginManager().isPluginEnabled("packetevents")) {
            getLogger().severe("====================================================");
            getLogger().severe("ERROR: Dependency PacketEvents not found!");
            getLogger().severe("Plugin IMiniMessage requires PacketEvents to work.");
            getLogger().severe("Please download and install PacketEvents:");
            getLogger().severe("https://www.spigotmc.org/resources/packetevents-api.80279/");
            getLogger().severe("====================================================");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.configManager = new ConfigManager(this);
        this.configManager.loadConfig();

        LegacyConverter legacyConverter = new LegacyConverter();
        ComponentProcessor componentProcessor = new ComponentProcessor(this.configManager, legacyConverter);

        this.packetListener = new PacketListener(componentProcessor);
        PacketEvents.getAPI().getEventManager().registerListener(this.packetListener);

        getServer().getPluginManager().registerEvents(new ChatListener(componentProcessor), this);

        getLogger().info("IMiniMessage " + getPluginMeta().getVersion() + " successfully enabled!");
    }

    @Override
    public void onDisable() {
        if (this.packetListener != null && getServer().getPluginManager().isPluginEnabled("packetevents")) {
            PacketEvents.getAPI().getEventManager().unregisterListener(this.packetListener);
        }
        getLogger().info("IMiniMessage " + getPluginMeta().getVersion() + " disabled.");
    }

    @SuppressWarnings("unused")
    public static IMiniMessage getInstance() {
        return instance;
    }

    @SuppressWarnings("unused")
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
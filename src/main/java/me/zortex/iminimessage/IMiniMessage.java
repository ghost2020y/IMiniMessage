package me.zortex.iminimessage;

import com.github.retrooper.packetevents.PacketEvents;
import me.zortex.iminimessage.converter.LegacyConverter;
import me.zortex.iminimessage.listener.PacketListener;
import me.zortex.iminimessage.listener.ChatListener;
import me.zortex.iminimessage.manager.ConfigManager;
import me.zortex.iminimessage.processor.ComponentProcessor;
import org.bukkit.plugin.java.JavaPlugin;

public final class IMiniMessage extends JavaPlugin {

    private static IMiniMessage instance;
    private ConfigManager configManager;
    private PacketListener packetListener;

    @Override
    public void onEnable() {
        instance = this;

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
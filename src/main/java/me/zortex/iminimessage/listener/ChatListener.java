package me.zortex.iminimessage.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.zortex.iminimessage.processor.ComponentProcessor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final ComponentProcessor componentProcessor;

    public ChatListener(ComponentProcessor componentProcessor) {
        this.componentProcessor = componentProcessor;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncChat(AsyncChatEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("iminimessage.use")) {
            Component originalMessage = event.message();
            Component processedMessage = componentProcessor.process(originalMessage);
            event.message(processedMessage);
        }
    }
}
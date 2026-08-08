package me.zortex.iminimessage.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import me.zortex.iminimessage.processor.ComponentProcessor;
import net.kyori.adventure.text.Component;

public class PacketListener extends PacketListenerAbstract {

    private final ComponentProcessor componentProcessor;

    public PacketListener(ComponentProcessor componentProcessor) {
        this.componentProcessor = componentProcessor;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
            WrapperPlayServerSystemChatMessage wrapper = new WrapperPlayServerSystemChatMessage(event);
            Component message = wrapper.getMessage();

            Component processedMessage = componentProcessor.process(message);
            wrapper.setMessage(processedMessage);
        }
    }
}
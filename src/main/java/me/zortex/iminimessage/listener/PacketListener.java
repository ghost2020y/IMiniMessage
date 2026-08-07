package me.zortex.iminimessage.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisguisedChat;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import me.zortex.iminimessage.processor.ComponentProcessor;

public class PacketListener extends PacketListenerAbstract {

    private final ComponentProcessor componentProcessor;

    public PacketListener(ComponentProcessor componentProcessor) {
        super(PacketListenerPriority.HIGH);
        this.componentProcessor = componentProcessor;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
            WrapperPlayServerSystemChatMessage packet = new WrapperPlayServerSystemChatMessage(event);
            packet.setMessage(componentProcessor.process(packet.getMessage()));
        } else if (event.getPacketType() == PacketType.Play.Server.DISGUISED_CHAT) {
            WrapperPlayServerDisguisedChat packet = new WrapperPlayServerDisguisedChat(event);
            packet.setMessage(componentProcessor.process(packet.getMessage()));
        }
    }
}
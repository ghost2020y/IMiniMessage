package me.zortex.iminimessage.listener;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisguisedChat;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import me.zortex.iminimessage.processor.ComponentProcessor;

public class PacketListener extends PacketListenerAbstract {

    private final ComponentProcessor componentProcessor;

    public PacketListener(ComponentProcessor componentProcessor) {
        this.componentProcessor = componentProcessor;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
            WrapperPlayServerSystemChatMessage wrapper = new WrapperPlayServerSystemChatMessage(event);
            wrapper.setMessage(componentProcessor.process(wrapper.getMessage()));
        }
        else if (event.getPacketType() == PacketType.Play.Server.DISGUISED_CHAT) {
            WrapperPlayServerDisguisedChat wrapper = new WrapperPlayServerDisguisedChat(event);
            wrapper.setMessage(componentProcessor.process(wrapper.getMessage()));
        }
        else if (event.getPacketType() == PacketType.Play.Server.CHAT_MESSAGE) {
            WrapperPlayServerChatMessage wrapper = new WrapperPlayServerChatMessage(event);
            ChatMessage chatMessage = wrapper.getMessage();
            chatMessage.setChatContent(componentProcessor.process(chatMessage.getChatContent()));
        }
    }
}
package com.ChatUp.ChatUp.chat;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMapper {

    public ChatResponse toChatResponse(@NotNull Chat chat, String userId){
        return ChatResponse.builder()
                .id(chat.getId())
                .name(chat.getChatName(userId))
                .isReceiverOnline(chat.getReceiver().isUserOnline())
                .lastMessageTime(chat.getLastMessageTime())
                .senderId(userId)
                .receiverId(chat.getReceiver().getId())
                .unReadCount(chat.unreadMessages(userId))
                .build();
    }

}

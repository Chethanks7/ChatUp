package com.ChatUp.ChatUp.notification;

import com.ChatUp.ChatUp.message.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    private String chatId;
    private String content;
    private MessageType messageType;
    private String senderId;
    private String receiverId;
    private String chatName;
    private NotificationType type;
    private byte[] media;
}

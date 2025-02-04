package com.ChatUp.ChatUp.message;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageRequest {

    private String content ;
    private String SenderId;
    private String receiverId;
    private MessageType messageType;
    private String chatId;

}

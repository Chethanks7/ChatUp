package com.ChatUp.ChatUp.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  ChatResponse {

    private String id ;
    private String name ;
    private long unReadCount;
    private LocalDateTime lastMessageTime ;
    private boolean isReceiverOnline;
    private String senderId;
    private String receiverId;


}

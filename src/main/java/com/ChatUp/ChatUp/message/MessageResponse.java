package com.ChatUp.ChatUp.message;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {
    private Long Id;
    private String content;
    private MessageType type;
    private MessageState state;
    private String receiverId;
    private LocalDateTime createAt;
    private byte[] media ;


}

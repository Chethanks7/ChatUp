package com.ChatUp.ChatUp.chat;

import com.ChatUp.ChatUp.common.StringResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService ;

    @PostMapping
    public ResponseEntity<StringResponse> createChat(
            @RequestParam("sender-id") String senderId,
            @RequestParam("receiver-id") String receiverId
            ){
            final String chatId = chatService.createChat(senderId,receiverId);
            return ResponseEntity.ok(StringResponse.builder()
                            .response(chatId)
                            .build());
    }

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getChatsByReceiver(
            Authentication authentication
    ){
        return ResponseEntity.ok(chatService.getChatsByReceiverId(authentication));
    }
}


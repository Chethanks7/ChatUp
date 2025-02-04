package com.ChatUp.ChatUp.chat;

import com.ChatUp.ChatUp.user.User;
import com.ChatUp.ChatUp.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository ;
    private final ChatMapper chatMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ChatResponse> getChatsByReceiverId(@NotNull Authentication connectedUser){
        final String userId = connectedUser.getName();
        return chatRepository.findChatsByUserId(userId).stream()
                .map(chat -> chatMapper.toChatResponse(chat,userId))
                .toList();
    }

    public String createChat(String senderId, String receiveId){
        Optional<Chat> existingChat = chatRepository.findChatBySenderAndReceiver(senderId,receiveId);
        if(existingChat.isPresent())
            return existingChat.get().getId();

        User sender = userRepository.findBYPublicId (senderId)
                .orElseThrow(() -> new EntityNotFoundException("User not found "));
        User receiver = userRepository.findBYPublicId (receiveId)
                .orElseThrow(() -> new EntityNotFoundException("User not found "));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);

        Chat savedChat = chatRepository.save(chat);
        return savedChat.getId();
    }
 }

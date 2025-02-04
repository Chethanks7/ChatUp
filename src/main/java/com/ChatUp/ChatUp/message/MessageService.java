package com.ChatUp.ChatUp.message;

import com.ChatUp.ChatUp.chat.Chat;
import com.ChatUp.ChatUp.chat.ChatRepository;
import com.ChatUp.ChatUp.file.FileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository ;
    private final ChatRepository chatRepository;
    private final MessageMapper mapper;
    private final FileService fileService;

    public void saveMessage(MessageRequest messageRequest){

        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("chat not found"));

        Message message = Message.builder()
                .chat(chat)
                .content(messageRequest.getContent())
                .senderId(messageRequest.getSenderId())
                .receiverId(messageRequest.getReceiverId())
                .type(messageRequest.getMessageType())
                .state(MessageState.SENT)
                .build();
        messageRepository.save(message);

        //todo notification
    }

    public List<MessageResponse> chatList(String chatId){
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(mapper::toMessageResponse)
                .toList();

    }

    @Transactional
    public void setMessagesToSeen(String chatId, Authentication authentication){
        Chat chat = chatRepository.findById(chatId).orElseThrow(
                () -> new EntityNotFoundException("Chat not found")
        );
        //final String receiverId = getReceiverId(chat,authentication);

        messageRepository.setMessagesToSeenByChatId(chatId,MessageState.SEEN);

        //todo notification
    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication){
        Chat chat = chatRepository.findById(chatId).orElseThrow(
                () -> new EntityNotFoundException("Chat not found")
        );

        final String senderId = getSenderId(chat, authentication);
        final String receiverId = getReceiverId(chat,authentication);

        final String filePath = fileService.saveFile(file,senderId);

        Message message = Message.builder()
                .chat(chat)
                .senderId(senderId)
                .receiverId(receiverId)
                .type(MessageType.IMAGE)
                .state(MessageState.SENT)
                .mediaFilePath(filePath)
                .build();

        messageRepository.save(message);

        //todo notification

    }

    private String getSenderId(@NotNull Chat chat, Authentication authentication) {
        if(chat.getSender().getId().equals(authentication.getName()))
            return chat.getSender().getId();
        return chat.getReceiver().getId();
    }

    private String getReceiverId(@NotNull Chat chat, Authentication authentication) {
        if(chat.getSender().getId().equals(authentication.getName()))
            return chat.getReceiver().getId();
        return chat.getSender().getId();
    }

}

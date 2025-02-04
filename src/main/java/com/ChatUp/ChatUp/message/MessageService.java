package com.ChatUp.ChatUp.message;

import com.ChatUp.ChatUp.chat.Chat;
import com.ChatUp.ChatUp.chat.ChatRepository;
import com.ChatUp.ChatUp.file.FileService;
import com.ChatUp.ChatUp.file.FileUtils;
import com.ChatUp.ChatUp.notification.Notification;
import com.ChatUp.ChatUp.notification.NotificationService;
import com.ChatUp.ChatUp.notification.NotificationType;
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
    private final NotificationService notificationService;

    public void saveMessage(MessageRequest messageRequest){

        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("chat not found"));

        Message message = new Message();
        message.setContent(messageRequest.getContent());
        message.setChat(chat);
        message.setSenderId(messageRequest.getSenderId());
        message.setReceiverId(messageRequest.getReceiverId());
        message.setType(messageRequest.getMessageType());
        message.setState(MessageState.SENT);
        messageRepository.save(message);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(messageRequest.getMessageType())
                .senderId(messageRequest.getSenderId())
                .content(messageRequest.getContent())
                .receiverId(messageRequest.getReceiverId())
                .type(NotificationType.MESSAGE)
                .chatName(chat.getChatName(message.getSenderId()))
                .build();

        notificationService.sendNotification(messageRequest.getReceiverId(),notification);

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

        final String receiverId = getReceiverId(chat,authentication);

        messageRepository.setMessagesToSeenByChatId(chatId,MessageState.SEEN);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .senderId(getSenderId(chat,authentication))
                .receiverId(receiverId)
                .type(NotificationType.SEEN)
                .build();

        notificationService.sendNotification(receiverId,notification);

    }

    public void uploadMediaMessage(String chatId, MultipartFile file, Authentication authentication){
        Chat chat = chatRepository.findById(chatId).orElseThrow(
                () -> new EntityNotFoundException("Chat not found")
        );

        final String senderId = getSenderId(chat, authentication);
        final String receiverId = getReceiverId(chat,authentication);

        final String filePath = fileService.saveFile(file,senderId);

        Message message = new Message();
        message.setReceiverId(receiverId);
        message.setSenderId(senderId);
        message.setState(MessageState.SENT);
        message.setType(MessageType.IMAGE);
        message.setMediaFilePath(filePath);
        message.setChat(chat);
        messageRepository.save(message);

        messageRepository.save(message);

        Notification notification = Notification.builder()
                .chatId(chat.getId())
                .messageType(MessageType.IMAGE)
                .senderId(senderId)
                .receiverId(receiverId)
                .type(NotificationType.IMAGE)
                .media(FileUtils.readFileFromLocation(filePath))
                .build();

        notificationService.sendNotification(receiverId,notification);

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

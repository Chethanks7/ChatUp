package com.ChatUp.ChatUp.message;

import com.ChatUp.ChatUp.file.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {

    public MessageResponse toMessageResponse(@NotNull Message message) {
        return MessageResponse.builder()
                .Id(message.getId())
                .content(message.getContent())
                .createAt(message.getCreatedDate())
                .receiverId(message.getReceiverId())
                .state(message.getState())
                .type(message.getType())
                .media(FileUtils.readFileFromLocation(message.getMediaFilePath()))
                .build();
    }


}

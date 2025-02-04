package com.ChatUp.ChatUp.chat;

import com.ChatUp.ChatUp.message.Message;
import com.ChatUp.ChatUp.message.MessageConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,String> {
    @Query(name = ChatConstant.FIND_CHAT_BY_SENDER_ID)
    List<Chat> findChatsByUserId(@Param("senderId") String userId);

    @Query(name = ChatConstant.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER_ID)
    Optional<Chat> findChatBySenderAndReceiver(String senderId, String receiveId);

}

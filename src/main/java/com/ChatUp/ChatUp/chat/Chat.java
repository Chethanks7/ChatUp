package com.ChatUp.ChatUp.chat;

import com.ChatUp.ChatUp.common.BaseAuditingEntity;
import com.ChatUp.ChatUp.message.Message;
import com.ChatUp.ChatUp.message.MessageState;
import com.ChatUp.ChatUp.message.MessageType;
import com.ChatUp.ChatUp.user.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chats")
@NamedQuery(name = ChatConstant.FIND_CHAT_BY_SENDER_ID,
            query = "SELECT DISTINCT c  FROM Chat c WHERE c.sender.id = :senderId OR c.receiver.id = :senderId ORDER BY createdDate DESC")
@NamedQuery(name = ChatConstant.FIND_CHAT_BY_SENDER_ID_AND_RECEIVER_ID,
        query = "SELECT DISTINCT c  FROM Chat c WHERE (c.sender.id = :senderId AND c.receiver.id = :receiverId)" +
                " OR (c.sender.id = :receiverId AND c.receiver.id = :senderId)")
@Tag(name = "Chat")
public class Chat extends BaseAuditingEntity {

    @Id
    @GeneratedValue(strategy = UUID)
    private String id ;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender ;
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER)
    @OrderBy("createdDate DESC")
    private List<Message> messages;

    @Transient
    public String getChatName(final String senderId){
        if(receiver.getId().equals(senderId))
            return sender.getFirstname() + " " + sender.getLastname();
        return receiver.getFirstname() + " " + receiver.getLastname();
    }

    @Transient
    public long unreadMessages(final String senderId){
        return messages.stream()
                .filter(m -> m.getReceiverId().equals(senderId))
                .filter(m-> MessageState.SENT == m.getState())
                .count();
    }

    @Transient
    public String getLastMessage(){
        if(messages !=null && !messages.isEmpty()){
            if(messages.get(0).getType() != MessageType.TEXT)
                return "attachment";
            return messages.get(0).getContent();
        }
        return null;
    }

    @Transient
    public LocalDateTime getLastMessageTime(){
        if(messages !=null && !messages.isEmpty()){
            return messages.get(0).getCreatedDate();
        }
        return null;
    }

}

package connectripbe.connectrip_be.chat.dto;

import connectripbe.connectrip_be.chat.entity.ChatMessage;
import connectripbe.connectrip_be.chat.entity.type.MessageType;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ChatMessageDto(
        String id,
        MessageType type,
        Long chatRoomId,
        Long senderId,
        String senderNickname,
        String senderProfileImage,
        String content,
        LocalDateTime createdAt
) {
    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .id(chatMessage.getId())
                .type(chatMessage.getType())
                .chatRoomId(chatMessage.getChatRoomId())
                .senderId(chatMessage.getSenderId())
                .senderNickname(chatMessage.getSenderNickname())
                .senderProfileImage(chatMessage.getSenderProfileImage())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}

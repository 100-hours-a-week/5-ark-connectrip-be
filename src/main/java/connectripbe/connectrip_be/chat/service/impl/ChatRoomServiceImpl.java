package connectripbe.connectrip_be.chat.service.impl;

import connectripbe.connectrip_be.chat.dto.ChatRoomListResponse;
import connectripbe.connectrip_be.chat.dto.ChatRoomMemberResponse;
import connectripbe.connectrip_be.chat.entity.ChatRoomMemberEntity;

import connectripbe.connectrip_be.chat.entity.type.ChatRoomMemberStatus;
import connectripbe.connectrip_be.chat.repository.ChatRoomMemberRepository;
import connectripbe.connectrip_be.chat.service.ChatRoomService;


import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

      private final ChatRoomMemberRepository chatRoomMemberRepository;

      /**
       * 사용자가 참여한 채팅방 목록을 조회하여 반환하는 메서드. 주어진 사용자의 이메일 주소를 기반으로 해당 사용자가 참여한 모든 채팅방을 조회
       *
       * @param memberId 사용자의 아이디. 이 이메일을 기반으로 해당 사용자가 참여한 채팅방을 조회
       * @return 사용자가 참여한 채팅방의 목록을 포함하는 `List<ChatRoomListResponse>` 사용자가 참여한 채팅방이 없을 경우 빈 리스트를 반환
       */
      @Override
      public List<ChatRoomListResponse> getChatRoomList(Long memberId) {
            // 사용자 참여한 모든 ChatRoomMemberEntity 조회
            List<ChatRoomMemberEntity> chatRoomMembers =  chatRoomMemberRepository.myChatRoomList(memberId);

            return chatRoomMembers.stream()
                    .map(member -> ChatRoomListResponse.fromEntity(member.getChatRoom()))
                    .toList();
      }

      /**
       * 주어진 채팅방의 참여자 목록을 조회하여 반환하는 메서드.
       * 주어진 채팅방의 ID를 기반으로 해당 채팅방의 모든 참여자를 조회
       *
       * @param chatRoomId 채팅방의 ID. 이 ID를 기반으로 해당 채팅방의 참여자를 조회
       * @return 채팅방의 참여자 목록을 포함하는 `List<ChatRoomMemberResponse>` 채팅방에 참여한 사용자가 없을 경우 빈 리스트를 반환
       */
      @Override
      public List<ChatRoomMemberResponse> getChatRoomMembers(Long chatRoomId) {

            List<ChatRoomMemberEntity> chatRoomMembers = chatRoomMemberRepository.findByChatRoom_Id(
                    chatRoomId);

            return chatRoomMembers.stream()
                    .filter(member -> !Objects.equals(member.getStatus(),
                            ChatRoomMemberStatus.EXIT))
                    .map(ChatRoomMemberResponse::fromEntity)
                    .toList();
      }
}

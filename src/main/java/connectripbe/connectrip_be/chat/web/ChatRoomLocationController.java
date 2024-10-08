package connectripbe.connectrip_be.chat.web;

import connectripbe.connectrip_be.chat.dto.SearchChatRoomWithToggleResponse;
import connectripbe.connectrip_be.chat.dto.UpdateChatRoomSharingRequest;
import connectripbe.connectrip_be.chat.service.ChatRoomLocationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// fixme-noah: 메서드명 수정 필요

@RestController
@RequestMapping("/api/v1/chatRoom")
@RequiredArgsConstructor
@Tag(name = "Chat Room Location", description = "채팅방 위치 공유 관리")
public class ChatRoomLocationController {

    private final ChatRoomLocationService chatRoomLocationService;

    // 채팅방 내 내 위치를 포함한 다른 동행자의 위치 조회
    @GetMapping("/{chatRoomId}/locations")
    public ResponseEntity<SearchChatRoomWithToggleResponse> getChatRoomLocations(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal Long memberId
    ) {
        SearchChatRoomWithToggleResponse searchChatRoomWithToggleResponse = chatRoomLocationService.searchChatRoomLocationByChatRoomIdAndMember(
                chatRoomId,
                memberId
        );

        return ResponseEntity.ok(searchChatRoomWithToggleResponse);
    }

    @PatchMapping("/{chatRoomId}/locations/sharing")
    public ResponseEntity<SearchChatRoomWithToggleResponse> updateChatRoomSharing(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal Long memberId,
            @RequestBody(required = false) UpdateChatRoomSharingRequest request
    ) {
        if (request == null) {
            SearchChatRoomWithToggleResponse searchChatRoomWithToggleResponse = chatRoomLocationService.updateNotShareLocation(
                    chatRoomId, memberId);

            return ResponseEntity.ok(searchChatRoomWithToggleResponse);
        } else {
            SearchChatRoomWithToggleResponse searchChatRoomWithToggleResponse = chatRoomLocationService.updateShareLocation(
                    chatRoomId, memberId, request.lat(), request.lng());

            return ResponseEntity.ok(searchChatRoomWithToggleResponse);
        }
    }

    // 채팅방 새로고침 버튼 API
    @PatchMapping("/{chatRoomId}/locations")
    public ResponseEntity<SearchChatRoomWithToggleResponse> updateChatRoomLocations(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long chatRoomId,
            @RequestBody UpdateChatRoomSharingRequest request
    ) {
        SearchChatRoomWithToggleResponse searchChatRoomWithToggleResponse = chatRoomLocationService.updateShareLocation(
                chatRoomId,
                memberId,
                request.lat(),
                request.lng()
        );

        return ResponseEntity.ok(searchChatRoomWithToggleResponse);
    }

    // 채팅방 내 위치 전송(갱신) API
    @PatchMapping("/{chatRoomId}/locations/me")
    public ResponseEntity<Void> updateChatRoomLocationsMe(
            @AuthenticationPrincipal Long memberId,
            @PathVariable Long chatRoomId,
            @RequestBody UpdateChatRoomSharingRequest request
    ) {
        chatRoomLocationService.updateMyLocation(memberId, chatRoomId, request.lat(), request.lng());

        return ResponseEntity.ok().build();
    }
}

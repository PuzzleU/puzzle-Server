package com.PuzzleU.Server.notify.dto;

import com.PuzzleU.Server.notify.entity.NotifyFriendShip;
import lombok.*;
// send()에서 sse를 클라이언트에게 전송할 때, 이벤트의 데이터로 전송할 dto
public class NotifyDto {
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Response {
        String id;
        String name;
        String content;
        String type;
        String createdAt;
        public static Response createResponse(NotifyFriendShip notifyFriendShip) {
            return Response.builder()
                    .content(notifyFriendShip.getContent())
                    .id(notifyFriendShip.getId().toString())
                    .name(notifyFriendShip.getUser().getUserKoreaName())
                    .createdAt(notifyFriendShip.getCreatedDate().toString())
                    .build();

        }
    }
}

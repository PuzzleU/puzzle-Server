package com.PuzzleU.Server.notify.dto;

import com.PuzzleU.Server.notify.entity.Notify;
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
        public static Response createResponse(Notify notify) {
            return Response.builder()
                    .content(notify.getContent())
                    .id(notify.getId().toString())
                    .name(notify.getUser().getUserKoreaName())
                    .createdAt(notify.getCreatedDate().toString())
                    .build();

        }
    }
}

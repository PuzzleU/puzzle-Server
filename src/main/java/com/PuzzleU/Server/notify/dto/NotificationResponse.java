package com.PuzzleU.Server.notify.dto;

import com.PuzzleU.Server.notify.entity.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationResponse {
    // 알림 id
    private Long id;

    // 알림 내용
    private String content;

    // 알림 클릭 시 이동할 url
    private String url;

    // 알림이 생성된 날짜
    private LocalDateTime createAt;

    // 알림 읽음 여부
    private boolean read;



    @Builder
    public NotificationResponse(Long id, String content, String url, LocalDateTime createAt, boolean read) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.createAt = createAt;
        this.read = read;
    }
    public static NotificationResponse from(Notification notification)
    {
        return NotificationResponse.builder()
                .id(notification.getId())
                .content(notification.getContent())
                .url(notification.getUrl())
                .createAt(notification.getCreatedDate())
                .read(notification.getIsRead())
                .build();
    }
}

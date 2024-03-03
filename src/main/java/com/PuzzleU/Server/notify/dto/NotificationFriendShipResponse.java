package com.PuzzleU.Server.notify.dto;

import com.PuzzleU.Server.notify.entity.NotifyFriendShip;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationFriendShipResponse {
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
    public NotificationFriendShipResponse(Long id, String content, String url, LocalDateTime createAt, boolean read) {
        this.id = id;
        this.content = content;
        this.url = url;
        this.createAt = createAt;
        this.read = read;
    }
    public static NotificationFriendShipResponse from(NotifyFriendShip notifyFriendShip)
    {
        return NotificationFriendShipResponse.builder()
                .id(notifyFriendShip.getId())
                .content(notifyFriendShip.getContent())
                .url(notifyFriendShip.getUrl())
                .createAt(notifyFriendShip.getCreatedDate())
                .read(notifyFriendShip.getIsRead())
                .build();
    }
}

package com.PuzzleU.Server.notification.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NotificationResponses {

    // 로그인 한 유저의 모든 알림
    private List<NotificationResponse> notificationFriendShipResponseList;

    // 로그인 한 유저가 읽지 않은 알림 수
    private long unreadCount;

    @Builder
    public NotificationResponses(List<NotificationResponse> notificationFriendShipResponseList, long uncreadCount)
    {
        this.notificationFriendShipResponseList = notificationFriendShipResponseList;
        this.unreadCount = uncreadCount;
    }
    public static NotificationResponses of(List<NotificationResponse> notificationResponseList, long count)
    {
        return NotificationResponses.builder()
                .notificationFriendShipResponseList(notificationResponseList)
                .uncreadCount(count)
                .build();
    }
}

package com.PuzzleU.Server.notify.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NotificationFriendShipsResponse {

    // 로그인 한 유저의 모든 알림
    private List<NotificationFriendShipResponse> notificationFriendShipResponseList;

    // 로그인 한 유저가 읽지 않은 알림 수
    private long unreadCount;

    @Builder
    public NotificationFriendShipsResponse(List<NotificationFriendShipResponse> notificationFriendShipResponseList, long uncreadCount)
    {
        this.notificationFriendShipResponseList = notificationFriendShipResponseList;
        this.unreadCount = uncreadCount;
    }
    public static NotificationFriendShipsResponse of(List<NotificationFriendShipResponse> notificationFriendShipResponseList, long count)
    {
        return NotificationFriendShipsResponse.builder()
                .notificationFriendShipResponseList(notificationFriendShipResponseList)
                .uncreadCount(count)
                .build();
    }
}

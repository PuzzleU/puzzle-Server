package com.PuzzleU.Server.notify.dto;

import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetNotificationsRes {
    private Long id;
    private String content;
    private Boolean isRead;
    private NotificationType notificationType;
    private User receiver;


    public GetNotificationsRes(Long id, String content, Boolean isRead, NotificationType notificationType, User receiver) {
        this.id = id;
        this.content = content;
        this.isRead = isRead;
        this.notificationType = notificationType;
        this.receiver = receiver;
    }
}

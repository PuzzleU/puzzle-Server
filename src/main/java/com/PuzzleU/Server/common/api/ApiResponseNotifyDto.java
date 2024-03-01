package com.PuzzleU.Server.common.api;

import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.notify.annotation.NotifyInfo;
import com.PuzzleU.Server.notify.entity.Notify;
import com.PuzzleU.Server.user.entity.User;
import lombok.Builder;
import lombok.Getter;


@Getter
public class ApiResponseNotifyDto<T>{
    private boolean success;
    private T response;
    private ErrorResponse error;
    private String jwt;
    private Notify notify;
    private NotifyInfo notifyInfo;

    @Builder
    ApiResponseNotifyDto(boolean success, T response, ErrorResponse error, String jwt, Notify notify, NotifyInfo notifyInfo) {
        this.success = success;
        this.response = response;
        this.error = error;
        this.jwt = jwt;
        this.notify = notify;
        this.notifyInfo = notifyInfo;
    }


}


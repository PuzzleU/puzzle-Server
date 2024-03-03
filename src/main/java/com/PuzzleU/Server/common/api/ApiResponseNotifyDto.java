package com.PuzzleU.Server.common.api;

import com.PuzzleU.Server.notify.annotation.NotifyInfo;
import com.PuzzleU.Server.notify.entity.NotifyFriendShip;
import lombok.Builder;
import lombok.Getter;


@Getter
public class ApiResponseNotifyDto<T>{
    private boolean success;
    private T response;
    private ErrorResponse error;
    private String jwt;
    private NotifyFriendShip notifyFriendShip;
    private NotifyInfo notifyInfo;

    @Builder
    ApiResponseNotifyDto(boolean success, T response, ErrorResponse error, String jwt, NotifyFriendShip notifyFriendShip, NotifyInfo notifyInfo) {
        this.success = success;
        this.response = response;
        this.error = error;
        this.jwt = jwt;
        this.notifyFriendShip = notifyFriendShip;
        this.notifyInfo = notifyInfo;
    }


}


package com.PuzzleU.Server.common.api;

import com.PuzzleU.Server.notify.annotation.NotifyInfo;
import com.PuzzleU.Server.notify.entity.NotifyFriendShip;

public class ResponseNotifyUtils {
    public static <T> ApiResponseNotifyDto<T> ok(T response, String jwt, NotifyFriendShip notifyFriendShip, NotifyInfo notifyInfo) {
        return ApiResponseNotifyDto.<T>builder()
                .success(true)
                .response(response)
                .jwt(jwt)
                .notify(notifyFriendShip)
                .notifyInfo(notifyInfo)
                .build();
    }

    public static <T> ApiResponseNotifyDto<T> error(ErrorResponse response) {
        return ApiResponseNotifyDto.<T>builder()
                .success(false)
                .error(response)
                .notify(null)
                .build();
    }
}


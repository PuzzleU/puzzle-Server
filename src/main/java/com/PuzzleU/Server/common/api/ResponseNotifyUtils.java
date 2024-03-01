package com.PuzzleU.Server.common.api;

import com.PuzzleU.Server.notify.annotation.NotifyInfo;
import com.PuzzleU.Server.notify.entity.Notify;

public class ResponseNotifyUtils {
    public static <T> ApiResponseNotifyDto<T> ok(T response, String jwt,Notify notify,NotifyInfo notifyInfo) {
        return ApiResponseNotifyDto.<T>builder()
                .success(true)
                .response(response)
                .jwt(jwt)
                .notify(notify)
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


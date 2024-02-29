package com.PuzzleU.Server.notify.dto;

import lombok.*;

public enum NotifyMessage {
    NEW_FRIEND("새로운 친구 요청이 있습니다");
    private String message;

    NotifyMessage(String message)
    {
        this.message = message;
    }
    public String getMessage()
    {
        return message;
    }

}

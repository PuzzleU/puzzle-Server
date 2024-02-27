package com.PuzzleU.Server.common.enumSet;

public enum NotifyMessage {

    FRIEND_REGISTER("친구 요청이 있습니다");
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

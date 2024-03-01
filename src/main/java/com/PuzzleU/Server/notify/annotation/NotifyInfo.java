package com.PuzzleU.Server.notify.annotation;

import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.user.entity.User;

public interface NotifyInfo {
    User getReciever();
    Long getGoUrlId();
    NotificationType getNotificationType();
}

package com.PuzzleU.Server.notify.controller;

import com.PuzzleU.Server.notify.service.NotifyService;
import com.PuzzleU.Server.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notify")
public class NotificationController {

    private final NotifyService notifyService;

    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable Long id,
                                // 받은 마지막 이벤트 ID값을 넘겨 그 이후의 데이터(받지 못한 데이터)부터 받을 수 있게 할 수 있는 정보
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue ="")String lastEventId)
    {
        return notifyService.subscribe(id, lastEventId);
    }
}

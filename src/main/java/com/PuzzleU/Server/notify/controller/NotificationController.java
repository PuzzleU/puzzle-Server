package com.PuzzleU.Server.notify.controller;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.notify.dto.NotificationResponses;
import com.PuzzleU.Server.notify.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@AllArgsConstructor
@RequestMapping("/api/notify")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetails user,
                                // 받은 마지막 이벤트 ID값을 넘겨 그 이후의 데이터(받지 못한 데이터)부터 받을 수 있게 할 수 있는 정보
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue ="")String lastEventId)
    {
        return notificationService.subscribe(user, lastEventId);
    }
    @GetMapping("/notifications")
    public ApiResponseDto<NotificationResponses> notifications(@AuthenticationPrincipal UserDetails loginUser)
    {
        return notificationService.findAllById(loginUser);
    }

    @PatchMapping("/notifications/{id}")
    public ApiResponseDto<SuccessResponse> readNotification(@PathVariable Long id)
    {
        return notificationService.readNotification(id);
    }
}

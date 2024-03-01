package com.PuzzleU.Server.notify.aop;

import com.PuzzleU.Server.common.api.ApiResponseNotifyDto;
import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.notify.annotation.NotifyInfo;
import com.PuzzleU.Server.notify.dto.NotifyMessage;
import com.PuzzleU.Server.notify.service.NotifyService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@EnableAsync
public class NotifyAspect {

    private final NotifyService notifyService;
    public NotifyAspect(NotifyService notifyService) {
        this.notifyService = notifyService;
    }


    // 아래의 어노테이션이 적용된 메소드들을 대상으로 AOP 적용가능
    @Pointcut("@annotation(com.PuzzleU.Server.notify.annotation.NeedNotify)")
    public void annotationPointcut()
    {

    }
    // @NeedNotify 어노테이션이 적용된 메서드 실행 후 알림 전송
    @Async
    @AfterReturning(pointcut = "annotationPointcut()", returning = "result")
    public void checkValue(JoinPoint jointPoint, Object result) throws Throwable {
        if (result instanceof ApiResponseNotifyDto) {
            ApiResponseNotifyDto<?> apiResponse = (ApiResponseNotifyDto<?>) result;
            NotifyInfo notifyProxy = apiResponse.getNotifyInfo();
            if (notifyProxy != null) {
                notifyService.send(
                        notifyProxy.getReciever(),
                        NotificationType.Friend,
                        NotifyMessage.NEW_FRIEND.getMessage(),
                        "/api/notify" + (notifyProxy.getGoUrlId())
                );
                System.out.println("asdsadsadsadsad");
                log.info("result = {}", result);
            }
        }
    }


}

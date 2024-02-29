package com.PuzzleU.Server.notify.aop;

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
@Aspect
@Slf4j
@ComponentScan
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
    @Async
    @AfterReturning(pointcut = "annotationPointcut()", returning = "result")
    public void checkValue(JoinPoint jointPoint, Object result) throws Throwable{
        NotifyInfo notifyProxy = (NotifyInfo) result;
        notifyService.send(
                notifyProxy.getReciever(),
                notifyProxy.getNotificationType(),
                NotifyMessage.NEW_FRIEND.getMessage(),
                "/api/notify" +(notifyProxy.getGoUrlId())
        );
        log.info("result = {}", result);
    }

}

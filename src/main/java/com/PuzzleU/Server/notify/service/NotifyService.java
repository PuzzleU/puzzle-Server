package com.PuzzleU.Server.notify.service;

import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.notify.dto.NotifyDto;
import com.PuzzleU.Server.notify.entity.Notify;
import com.PuzzleU.Server.notify.repository.EmitterRepository;
import com.PuzzleU.Server.notify.repository.NotifyRepository;
import com.PuzzleU.Server.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
public class NotifyService {

    // ssemitter -> 서버에서 클라이언트로의 단뱡향 통신 지원
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotifyRepository notifyRepository;

    // SSE를 구독할 때 호출
    // username을 기반으로 SseEmitter를 생성하고, EmitterRepository를 통해 저장
    // 클라이언트와의 연결이 끊기면/타임아웃이 발생하면, 해당 SseEmitter를 제거
    public SseEmitter subscribe(String username, String lastEventId)
    {

        String emitterId = makeTimeInclude(username);
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        emitter.onCompletion(() -> emitterRepository.deletedById(emitterId));
        emitter.onTimeout(()-> emitterRepository.deletedById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String evenId = makeTimeInclude(username);
        sendNotification(emitter, evenId, emitterId, "EventStream Created. [userEmail=" + username + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId))
        {
            sendLostData(lastEventId, username, emitterId, emitter);
        }
        return emitter;
    }

    // 이메일과 현재시간을 조합하여 고유한 식별자 생성
    private String makeTimeInclude(String email)
    {
        return email + "_" + System.currentTimeMillis();
    }
    // SseEmitter를 통해 이벤트 전송
    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data)
    {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            );
        } catch (IOException exception)
        {
            emitterRepository.deletedById(emitterId);
        }
    }
    // 클라이언트가 미수신한 이벤트가 있는지 확인
    private boolean hasLostData(String lastEventId)
    {
        return !lastEventId.isEmpty();
    }
    // 클라이언트가 미수신한 이벤트를 재전송하여 데이터 유실을 방지
    private void sendLostData(String lastEventId, String userEmail, String emitterId, SseEmitter emitter)
    {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userEmail));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey())<0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }
    // 사용자에게 알림을 보냄
    // Notify 엔터티 생성후 DB 저장
    // 모든 SseEmitter에 알림을 전송
    // 알림을 전송한 후에는 해당 이벤트를 캐싱
    public void send(User receiver, NotificationType notificationType, String content, String url)
    {
        System.out.println("a");
        Notify notification = notifyRepository.save(createNotification(receiver,notificationType,content,url));

        String receiverEmail = receiver.getUserKoreaName();

        String eventId = receiverEmail + "_" + System.currentTimeMillis();
        Map<String, SseEmitter>emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverEmail);
        emitters.forEach(
                (key, emitter) -> {
                    emitterRepository.saveEventCache(key, notification);
                    sendNotification(emitter, eventId, key, NotifyDto.Response.createResponse(notification));
                }
        );
    }

    // 알림 엔터티를 생성
    private Notify createNotification(User receiver, NotificationType notificationType, String content, String url)
    {
        return Notify.builder()
                .user(receiver)
                .notificationType(notificationType)
                .content(content)
                .url(url)
                .isRead(false)
                .build();
    }
}

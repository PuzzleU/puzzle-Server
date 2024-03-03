package com.PuzzleU.Server.notify.service;

import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.notify.dto.NotificationFriendShipResponse;
import com.PuzzleU.Server.notify.dto.NotificationFriendShipsResponse;
import com.PuzzleU.Server.notify.entity.NotifyFriendShip;
import com.PuzzleU.Server.notify.repository.EmitterRepository;
import com.PuzzleU.Server.notify.repository.NotifyFriendShipRepository;
import com.PuzzleU.Server.notify.repository.NotifyRepository;
import com.PuzzleU.Server.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private static final Logger log = LoggerFactory.getLogger(NotifyService.class);
    private static final Long DEFAULT_TIME = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final NotifyFriendShipRepository notifyFriendShipRepository;

    public SseEmitter subscribe(User user, String lastEventId)
    {
        Long userId = user.getId();
        String id = userId + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIME));

        emitter.onCompletion(()-> emitterRepository.deletedById(id));
        emitter.onTimeout(()-> emitterRepository.deletedById(id));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" +userId + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if(!lastEventId.isEmpty())
        {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithByMemberId(String.valueOf(userId));
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey())<0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;
    }

    private void sendToClient(SseEmitter emitter, String id, Object data)
    {
        try{
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch(IOException exception)
        {
            emitterRepository.deletedById(id);
            log.error("SSE 연결 오류!", exception);
        }
    }

    @Transactional
    public void sendFriend(User receiver, FriendShip friendShip, String content, NotificationType notificationType)
    {
        NotifyFriendShip notifyFriendShip = createNotification(receiver,friendShip,content, NotificationType.Friend);
        String id = String.valueOf(receiver.getId());
        notifyFriendShipRepository.save(notifyFriendShip);
        Map<String, SseEmitter>sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(id);
        sseEmitters.forEach(
                (key, emitter) ->{
                    emitterRepository.saveEventCache(key, notifyFriendShip);
                    sendToClient(emitter, key, NotificationFriendShipResponse.from(notifyFriendShip));
                }
        );

    }

    private NotifyFriendShip createNotification(User receiver, FriendShip friendShip, String content, NotificationType notificationType)
    {
        return NotifyFriendShip.builder()
                .user(receiver)
                .content(content)
                .url("/friendship/" + friendShip.getFriendShipId())
                .isRead(false)
                .notificationType(notificationType)
                .friendShip(friendShip)
                .build();
    }
    @Transactional
    public NotificationFriendShipsResponse findAllById(User user)
    {
        List<NotificationFriendShipResponse> responses = notifyFriendShipRepository.findAllByUserId(user.getId()).stream()
                .map(NotificationFriendShipResponse::from)
                .collect(Collectors.toList());
        long unreadCount = responses.stream()
                .filter(notification -> !notification.isRead())
                .count();
        return NotificationFriendShipsResponse.of(responses, unreadCount);
    }

    @Transactional
    public void readNotification(Long id)
    {
        NotifyFriendShip notification = notifyFriendShipRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 알림"));
        notification.read();
    }
}

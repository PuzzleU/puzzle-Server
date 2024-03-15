package com.PuzzleU.Server.notify.service;

import com.PuzzleU.Server.apply.entity.Apply;
import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.enumSet.NotificationType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.friendship.repository.FriendshipRepository;
import com.PuzzleU.Server.notify.dto.NotificationResponses;
import com.PuzzleU.Server.notify.dto.NotificationResponse;
import com.PuzzleU.Server.notify.entity.Notification;
import com.PuzzleU.Server.notify.repository.EmitterRepository;
import com.PuzzleU.Server.notify.repository.NotificationRepository;
import com.PuzzleU.Server.team.entity.Team;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    String baseUrl = "https://server.puzzleu.store/";
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private static final Long DEFAULT_TIME = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final NotificationRepository notificationRepository;


    public SseEmitter subscribe(UserDetails loginuser, String lastEventId)
    {
        User user = userRepository.findByUsername(loginuser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
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
    public void sendFriend(User receiver, User giver, String content, NotificationType notificationType)
    {
        Notification notification = createFriendNotification(receiver,giver,content, notificationType);
        String id = String.valueOf(receiver.getId());
        notificationRepository.save(notification);
        Map<String, SseEmitter>sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(id);
        sseEmitters.forEach(
                (key, emitter) ->{
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }
    @Transactional
    public void sendApply(User receiver, User giver, String content, NotificationType notificationType, Team team, Competition competition)
    {
        Notification notification = createApplyNotification(receiver,giver,content, notificationType, competition, team);
        String id = String.valueOf(receiver.getId());
        notificationRepository.save(notification);
        Map<String, SseEmitter>sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(id);
        sseEmitters.forEach(
                (key, emitter) ->{
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }
    @Transactional
    public void sendTeamJoin(User receiver, User giver, String content, NotificationType notificationType, Team team, Competition competition)
    {
        Notification notification = createTeamJoinNotification(receiver,giver,content, notificationType, competition, team);
        String id = String.valueOf(receiver.getId());
        notificationRepository.save(notification);
        Map<String, SseEmitter>sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(id);
        sseEmitters.forEach(
                (key, emitter) ->{
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }

    @Transactional
    public void sendApplyStatusJoin(User receiver, User giver, String content, NotificationType notificationType, Apply apply)
    {
        Notification notification = createApplyStatusNotification(receiver,giver,content, notificationType, apply);
        String id = String.valueOf(receiver.getId());
        notificationRepository.save(notification);
        Map<String, SseEmitter>sseEmitters = emitterRepository.findAllEmitterStartWithByMemberId(id);
        sseEmitters.forEach(
                (key, emitter) ->{
                    emitterRepository.saveEventCache(key, notification);
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }



    // 친구가 요청을 보냈을 때의 Notification을 보여주고 저장한다.
    private Notification createFriendNotification(User receiver,User giver, String content, NotificationType notificationType)
    {
        Optional<FriendShip> friendShipOptional = friendshipRepository.findByUser1AndUser2(receiver, giver);
        FriendShip friendShip = friendShipOptional.orElseThrow(
                ()-> new RestApiException(ErrorType.NOT_FOUND_FRIENDSHIP)
        );
        return Notification.builder()
                .user(receiver)
                .content(content)
                .url(baseUrl + "/api/user/profile/"+giver.getId())
                .isRead(false)
                .notificationType(notificationType)
                .build();
    }
    // 팀장한테 누군가 지원했다보 보내짐
    private Notification createApplyNotification(User receiver, User giver, String content, NotificationType notificationType, Competition competition, Team team)
    {
        return Notification.builder()
                .user(receiver)
                .content(content)
                // 그 팀으로 이동함
                .url(baseUrl + "/competition/"+competition.getCompetitionId() + "/team/" +team.getTeamId() )
                .isRead(false)
                .notificationType(notificationType)
                .build();
    }
    // 내가 어느 팀에 속하게 되었는지를 보여줌
    private Notification createTeamJoinNotification(User receiver, User giver, String content, NotificationType notificationType,Competition competition, Team team)
    {
        return Notification.builder()
                .user(receiver)
                .content(content)
                .url(baseUrl + "/competition/"+competition.getCompetitionId() + "/team/" +team.getTeamId())
                .isRead(false)
                .notificationType(notificationType)
                .build();
    }
    private Notification createApplyStatusNotification(User receiver,User giver, String content, NotificationType notificationType, Apply apply)
    {

        return Notification.builder()
                .user(receiver)
                .content(content)
                .url(baseUrl + "/api/apply/"+apply.getApplyId())
                .isRead(false)
                .notificationType(notificationType)
                .build();
    }


    @Transactional
    public ApiResponseDto<NotificationResponses> findAllById(UserDetails loginUser)
    {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        List<NotificationResponse> responses = notificationRepository.findByUserAndUnread(user).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());
        long unreadCount = responses.stream()
                .filter(notification -> !notification.isRead())
                .count();
        // 아직 안읽은 놈들의 개수를 보여준다
        return ResponseUtils.ok(NotificationResponses.of(responses, unreadCount),null);
    }

    @Transactional
    public ApiResponseDto<SuccessResponse> readNotification(Long id)
    {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 알림"));
        notification.read();
        notificationRepository.save(notification);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK,"모든 알람을 읽었습니다"), null);
    }
}

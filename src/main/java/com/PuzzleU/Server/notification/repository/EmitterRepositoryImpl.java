package com.PuzzleU.Server.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

    // ConcurrentHashMap(안전성 보장)을 사용하여 스레드 안전성을 보장하는 맵을 선언
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    // SseEmitter를 저장하는 메서드
    @Override
    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    // 이벤트 캐시를 저장하는 메서드
    @Override
    public void saveEventCache(String eventCacheId, Object event) {
        eventCache.put(eventCacheId, event);
    }

    // 사용자 ID로 시작하는 모든 SseEmitter를 반환하는 메서드
    @Override
    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String userId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 사용자 ID로 시작하는 모든 이벤트 캐시를 반환하는 메서드
    @Override
    public Map<String, Object> findAllEventCacheStartWithByMemberId(String userId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(userId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // 지정된 ID에 해당하는 SseEmitter를 삭제하는 메서드
    @Override
    public void deletedById(String id) {
        emitters.remove(id);
    }

    // 사용자 ID로 시작하는 모든 SseEmitter를 삭제하는 메서드
    @Override
    public void deleteAllEmitterStartWithId(String userId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(userId)) {
                        emitters.remove(key);
                    }
                }
        );
    }

    // 사용자 ID로 시작하는 모든 이벤트 캐시를 삭제하는 메서드
    @Override
    public void deleteAllEventCacheStartWithId(String userId) {
        eventCache.forEach(
                (key, emitters) -> {
                    if (key.startsWith(userId)) {
                        eventCache.remove(key);
                    }
                }
        );
    }
}

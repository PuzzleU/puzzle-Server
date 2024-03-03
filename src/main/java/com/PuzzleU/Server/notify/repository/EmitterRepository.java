package com.PuzzleU.Server.notify.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

public interface EmitterRepository {
    // Emitter 저장
    SseEmitter save(String emitterId, SseEmitter sseEmitter);
    // 이벤트를 저장한다
    void saveEventCache(String emitterId, Object event);
    // 해당 회원과 관련된 모든 Emitter를 찾는다 -> 브라우저당 여러개 연결이 가능하기에 여러 Emitter가 존재할 수 있다
    Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String userId);
    // 해당 회원과 관련된 모든 이벤트를 찾는다
    Map<String, Object > findAllEventCacheStartWithByMemberId(String userId);
    // Emitter를 지운다
    void deletedById(String id);
    // 해당 회원과 관련된 모든 Emitter를 지운다
    void deleteAllEmitterStartWithId(String userId);
    // 해당 회원과 관련된 모든 이벤트를 지운다
    void deleteAllEventCacheStartWithId(String userId);
}

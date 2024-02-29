package com.PuzzleU.Server.friendship.service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.friendship.entity.FriendShip;
import com.PuzzleU.Server.friendship.repository.FriendshipRepository;
import com.PuzzleU.Server.notify.annotation.NeedNotify;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    // 특정 유저에게 친구신청을 거는 것
    @NeedNotify
    @Transactional
    public ApiResponseDto<SuccessResponse> registerFriend(UserDetails loginUser, Long userId) {
        User user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        // friendship이 만들어져야한다
        // 무조건 거는 사람이 user1, 받는 사람이 user2로 되도록
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        // 만약에 user1과 2가 존재한다면 끝
        Optional<FriendShip> friendShip_check = friendshipRepository.findByUser1AndUser2(user1, user2);

        if (friendShip_check.isPresent()) {
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "이미 진행된 친구신청입니다."), null);
        } else {
            // 친구 관계가 존재하지 않는 경우, 새로운 친구 관계 생성
            FriendShip friendShip = new FriendShip();
            friendShip.setUser2(user2);
            friendShip.setUser1(user1);
            friendShip.setUserStatus(false);
            friendshipRepository.save(friendShip);
            return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구신청이 완료되었습니다"), null);
        }

    }
    @NeedNotify
    @Transactional
    public ApiResponseDto<SuccessResponse> responseFriend(UserDetails loginUser, Long userId) {
        System.out.println(loginUser);
        System.out.println(userId);
        User user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        System.out.println(user1);
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        System.out.println(user2);
        Optional<FriendShip> friendShip_check = friendshipRepository.findByUser1AndUser2(user1, user2);
        FriendShip friendShip = friendShip_check.orElseThrow(()-> new RestApiException(ErrorType.NOT_FOUND_FRIENDSHIP));
        System.out.println(friendShip);
        friendShip.setUserStatus(true);
        friendshipRepository.save(friendShip);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구수락이 완료되었습니다"), null);

    }
    @Transactional
    public ApiResponseDto<SuccessResponse> deleteFriend(UserDetails loginUser, Long userId) {
        User user1 = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<User> userOptional = userRepository.findById(userId);
        User user2 = userOptional.orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<FriendShip> friendShip_check = friendshipRepository.findByUser1AndUser2(user1, user2);
        FriendShip friendShip = friendShip_check.orElseThrow(()-> new RestApiException(ErrorType.NOT_FOUND_FRIENDSHIP));
        friendshipRepository.delete(friendShip);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "친구신청이 완료되었습니다"), null);

    }
}

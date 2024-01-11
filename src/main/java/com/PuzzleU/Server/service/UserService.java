package com.PuzzleU.Server.service;

public class UserService {

    // 1. ApiResponseDto 형식을 가진 method를 구현 (Success / Response / Error ) 존재
    // 2. Response의 형태가 미정이므로 SuccessResponse로 구현해준다
    // 3. 만약에 유저가 존재한다면 RestApiException에 추가 ( 여긴 에러 타입을 넣어야한다) - 중간에 에러가 뜬다면
    // 4. SignupRequestDto 에 있는 Boolean admin이 True일 경우 role은 UserRoleEnum.ADMIN이 된다
    // 5. return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"));
    // 응답은 ResponseUtils를 이용. 성공했으니까 ok 메소드를 써야겠지?
    // ok는 ApiResonseDto를 돌려준다 여기에는 success, error, 가 담긴다
    // 이때 response에는 status와 message가 담기는데 이것들이 HttpStatus.OK, "회원가입 성공" 다.
    /*
    @Transactional
    public ApiResponseDto<SuccessResponse> signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new RestApiException(ErrorType.DUPLICATED_USERNAME);
        }

        // 입력한 username, password, admin 으로 user 객체 만들어 repository 에 저장
        UserRoleEnum role = requestDto.getAdmin() ? UserRoleEnum.ADMIN : UserRoleEnum.USER;
        userRepository.save(User.of(username, password, role));

        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "회원가입 성공"));
    }
     */
}

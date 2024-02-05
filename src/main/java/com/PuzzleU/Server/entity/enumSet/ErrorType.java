package com.PuzzleU.Server.entity.enumSet;

import lombok.Getter;

@Getter
public enum ErrorType {
    NOT_VALID_TOKEN(400, "토큰이 유효하지 않습니다."),
    NOT_VALID_CLIENT(400, "애플 로그인 Feign API Clinet 호출 오류"),
    NOT_WRITER(400, "작성자만 삭제/수정할 수 있습니다."),
    DUPLICATED_USERNAME(400, "중복된 username 입니다."),
    NOT_MATCHING_INFO(400, "회원을 찾을 수 없습니다."),
    NOT_MATCHING_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(400, "사용자가 존재하지 않습니다."),
    NOT_FOUND_WRITING(400, "게시글/댓글이 존재하지 않습니다."),
    NOT_FOUND_SKILLSET(400, "스킬셋이 존재하지 않습니다"),
    NOT_FOUND_MAJOR(400, "전공이 존재하지 않습니다"),
    NOT_FOUND_EXPERIENCE(400, "경험이 존재하지 않습니다"),
    NOT_FOUND_UNIVERSITY(400, "대학이 존재하지 않습니다"),
    NOT_FOUND_USERSKILLSETRELATION(400, "유저와 스킬셋의 관계가 존재하지 않습니다"),
    NOT_FOUND_COMPETITION(400, "공모전이 존재하지 않습니다"),
    NOT_FOUND_POSITION_LIST(404, "포지션 리스트가 존재하지 않습니다."),
    NOT_FOUND_INTEREST_LIST(404, "관심 분야 리스트가 존재하지 않습니다."),
    NOT_FOUND_LOCATION_LIST(404, "지역 리스트가 존재하지 않습니다."),
    NOT_FOUND_PROFILE_LIST(404, "프로필 리스트가 존재하지 않습니다.");

    private int code;
    private String message;

    //  Error Type 생성자 생성
    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

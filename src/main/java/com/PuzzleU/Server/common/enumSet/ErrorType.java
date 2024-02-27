package com.PuzzleU.Server.common.enumSet;

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
    NOT_FOUND_POSITION(400, "포지션이 존재하지 않습니다."),
    NOT_FOUND_INTEREST(400, "관심 분야가 존재하지 않습니다."),
    NOT_FOUND_LOCATION(400, "지역이 존재하지 않습니다."),
    NOT_FOUND_PROFILE(400, "프로필이 존재하지 않습니다."),
    NOT_FOUND_COMPETITION(400, "공모전이 존재하지 않습니다"),
    NOT_FOUND_POSITION_LIST(404, "포지션 리스트가 존재하지 않습니다."),
    NOT_FOUND_INTEREST_LIST(404, "관심 분야 리스트가 존재하지 않습니다."),
    NOT_FOUND_LOCATION_LIST(404, "지역 리스트가 존재하지 않습니다."),
    NOT_FOUND_PROFILE_LIST(404, "프로필 리스트가 존재하지 않습니다."),
    TOO_MUCH_LOCATIONS(400, "너무 많은 지역이 입력되었습니다. 지역은 최대 2개까지 선택 가능합니다."),
    TOO_FEW_POSITIONS(400, "너무 적은 포지션이 입력되었습니다. 하나 이상의 포지션을 선택해야 합니다."),
    NAME_NOT_PROVIDED(400, "이름이 입력되지 않았습니다."),
    PUZZLE_ID_NOT_PROVIDED(400, "퍼즐 아이디가 입력되지 않았습니다."),
    PROFILE_NOT_PROVIDED(400, "프로필이 입력되지 않았습니다."),
    NOT_FOUND_FRIENDSHIP(400, "등록되지 않은 관계입니다."),
    NOT_FOUND_TEAM(400, "팀을 찾을 수 없습니다"),
    NOT_FOUND_RELATION(400, "올바르지 않은 관계입니다"),
    NOT_MATCHING_UNIVERSITY_TYPE(400, "학교 타입이 적절하게 설정되지 않았습니다. 학교 타입은 UNIVERSITY 또는 GRADUATE여야 합니다."),
    NOT_FOUND_APPLY(400, "해당 지원서가 없습니다"),
    NOT_PERMITTED(400, "권한이 없습니다"),
    NOT_FOUND_USER_TEAM(400, "회원님은 이 팀에 대한 권한이 없습니다"),
    NOT_EXPERIENCE_YET(400, "경험 정보를 모두 기입해 주세요"),
    NOT_FOUND(400, "해당 정보가 존재하지 않습니다." ),
    INTERNAL_SERVER_ERROR(500, "서버에러"),
    POSITION_NOT_PROVIDED(400, "포지션이 입력되지 않았습니다."),
    FOUND_LIKE(400, "이미 좋아요를 눌렀습니다"),
    NO_PERMISSION_TO_APPLICATION_LIST(403, "지원서 리스트를 열람할 권한이 없습니다."),
    NO_PERMISSION_TO_APPLICATION(403, "지원서를 열람할 권한이 없습니다."),
    NOT_MATCH_ACCEPT_REJECT(400, "요청값을 확인하세요. ACCEPT 또는 REJECT로만 요청 가능합니다."),
    NO_PERMISSION_TO_ACCEPT_APPLY(403, "지원서를 수락 또는 거절할 권한이 없습니다."),
    ALREADY_REGISTERED(400, "이미 지원한 공모전입니다"),
    NOT_ALLOWED_YEAR(400, "졸업예정 년도가 입학년도보다 빠를 순 없습니다"),
    ALREADY_SUBMIT_APPLY(400, "이미 지원서를 제출했습니다. 한 팀 당 하나의 지원서만 제출 가능합니다.");



    private int code;
    private String message;

    //  Error Type 생성자 생성
    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

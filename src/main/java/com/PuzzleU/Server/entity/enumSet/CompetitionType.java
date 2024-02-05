package com.PuzzleU.Server.entity.enumSet;

import lombok.Getter;

@Getter
public enum CompetitionType {
    PLAN("기획"),
    DESIGN("디자인"),
    LITURE("문학"),
    MEDIA("미디어"),
    MARKET("마케팅"),
    NAME("네이밍"),
    ART("예술"),
    IT("IT"),
    START("창업"),
    ETC("기타");
    private final String message;

    CompetitionType(String message) {
        this.message = message;
    }
}

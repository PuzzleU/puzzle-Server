package com.PuzzleU.Server.common.enumSet;

import lombok.Getter;

@Getter
public enum Term {
    AGE_TERM("ageTermConsent", TermType.REQUIRED, "만 18세 이상"),
    SERVICE_TERM("serviceTermConsent", TermType.REQUIRED, "서비스 이용 약관"),
    PERSONAL_INFO_TERM("personalInfoConsent", TermType.REQUIRED, "개인정보 수집 및 이용 동의"),
    SERVICE_NOTIFICATION_TERM("serviceNotificationConsent", TermType.REQUIRED, "서비스 알림 수신"),
    RECEIVE_MARKETING_TERM("receiveMarketingConsent", TermType.OPTIONAL, "혜택/이벤트 정보 수신 동의");

    private String termAlias;
    private TermType termType;
    private String termContent;

    Term(String termAlias, TermType termType, String termContent) {
        this.termAlias = termAlias;
        this.termType = termType;
        this.termContent = termContent;
    }
}

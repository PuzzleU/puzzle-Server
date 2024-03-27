package com.PuzzleU.Server.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class TermsConsentDto {
    private Boolean ageTermConsent;
    private Boolean serviceTermConsent;
    private Boolean personalInfoConsent;
    private Boolean serviceNotificationConsent;
    private Boolean receiveMarketingConsent;
}

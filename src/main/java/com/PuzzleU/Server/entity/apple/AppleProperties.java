package com.PuzzleU.Server.entity.apple;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
// application.properties에 자동으로 매핑해준다
@ConfigurationProperties(prefix = "social-login.provider.apple")
@Getter
@Setter
public class AppleProperties {

    private String grantType;
    private String clientId;
    private String keyId;
    private String teamId;
    private String audience;
    private String privateKey;
}

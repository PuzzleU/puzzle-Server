package com.PuzzleU.Server.dto.profile;

import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {
    private Long ProfileId;
    private String ProfileUrl;
}

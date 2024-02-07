package com.PuzzleU.Server.profile.dto;

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

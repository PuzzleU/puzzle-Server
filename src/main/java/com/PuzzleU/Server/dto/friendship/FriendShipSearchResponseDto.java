package com.PuzzleU.Server.dto.friendship;

import com.PuzzleU.Server.dto.user.UserSimpleDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendShipSearchResponseDto {
    // 유저의 이름, 한줄소개
    @JsonProperty("FriendList")
    private List<UserSimpleDto> userSimpleDtoList;

}

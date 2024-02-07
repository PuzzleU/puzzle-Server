package com.PuzzleU.Server.friendship.dto;

import com.PuzzleU.Server.user.dto.UserSimpleDto;
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
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

package com.PuzzleU.Server.team.dto;

import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 마감빠른 순 , 마감 느린 순, 인기 순, 팀빌 순
public class TeamListDto {

    private List<TeamAbstractDto> teamList;
    private int totalTeam;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

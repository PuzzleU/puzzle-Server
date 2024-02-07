package com.PuzzleU.Server.competition.dto;

import lombok.*;

import java.util.List;
@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionHomeTotalDto {
    private List<CompetitionHomePageDto> competitionList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

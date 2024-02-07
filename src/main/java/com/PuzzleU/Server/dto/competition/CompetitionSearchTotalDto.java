package com.PuzzleU.Server.dto.competition;

import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionSearchTotalDto {
    private List<CompetitionSearchDto> competitionSearchDtoList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

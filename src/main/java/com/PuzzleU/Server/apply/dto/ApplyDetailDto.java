package com.PuzzleU.Server.apply.dto;

import com.PuzzleU.Server.position.dto.PositionDto;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
public class ApplyDetailDto {
    private String CompetitionPoster;
    private String CompetitionTitle;
    private String TeamTitle;
    private List<PositionDto> PositionList;
    private String ApplyTitle;
    private String ApplyContent;

    @Builder
    public ApplyDetailDto(String competitionPoster, String competitionTitle, String teamTitle, List<PositionDto> positionList, String applyTitle, String applyContent) {
        CompetitionPoster = competitionPoster;
        CompetitionTitle = competitionTitle;
        TeamTitle = teamTitle;
        PositionList = positionList;
        ApplyTitle = applyTitle;
        ApplyContent = applyContent;
    }
}
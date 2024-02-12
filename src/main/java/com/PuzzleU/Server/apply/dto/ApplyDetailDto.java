package com.PuzzleU.Server.apply.dto;

import com.PuzzleU.Server.position.dto.PositionDto;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyDetailDto {
    private String CompetitionPoster;
    private String CompetitionTitle;
    private String TeamTitle;
    private List<PositionDto> PositionList;
    private String ApplyTitle;
    private String ApplyContent;
}

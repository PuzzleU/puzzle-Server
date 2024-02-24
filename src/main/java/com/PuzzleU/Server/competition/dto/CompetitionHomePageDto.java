package com.PuzzleU.Server.competition.dto;

import com.PuzzleU.Server.common.enumSet.CompetitionType;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionHomePageDto {
    private Long competitionId;
    @Size(max = 100)
    private String competitionName;
    private Integer CompetitionVisit;
    private Integer CompetitionMatching;
    private List<CompetitionType> competitionTypeList;
    private Integer CompetitionDday;
    private String createdAt;
    private Integer competitionHeart;
    private String competitionPoster;

}

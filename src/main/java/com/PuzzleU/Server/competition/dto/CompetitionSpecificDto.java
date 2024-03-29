package com.PuzzleU.Server.competition.dto;

import com.PuzzleU.Server.common.enumSet.CompetitionType;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionSpecificDto {

    private Long competitionId;
    @Size(max = 100)
    private String competitionName;
    @Size(max =200)
    private String competitionUrl;
    @Size(max = 50)
    private String competitionHost;
    @Size(max = 200)
    private String competitionPoster;
    @Size(max =50)
    private String competitionAwards;

    private String competitionStart;
    private String competitionEnd;
    @Size(max = 2000)
    private String competitionContent;
    private Integer competitionVisit;
    private Integer competitionLike;
    private Integer competitionMatching;
    private Integer competitionDDay;
    private List<CompetitionType> competitionTypes;











}

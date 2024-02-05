package com.PuzzleU.Server.dto.competition;

import com.PuzzleU.Server.entity.enumSet.CompetitionType;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;
@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionDto {

    @Size(max = 100)
    private String competitionName;
    @Size(max = 200)
    private String competitionUrl;
    @Size(max = 50)
    private String CompetitionHost;
    @Size(max = 200)
    private String CompetitionPoster;
    @Size(max = 50)
    private String CompetitionAwards;

    private Date CompetitionStart;
    private Date CompetitionEnd;
    private Date CompetitionContent;
    private Integer CompetitionVisit;
    private Integer CompetitionLike;
    private Integer CompetitionMatching;
    private Date CompetitionUpload;
    private Integer competitionDday;
    private List<CompetitionType> competitionTypeList;
}

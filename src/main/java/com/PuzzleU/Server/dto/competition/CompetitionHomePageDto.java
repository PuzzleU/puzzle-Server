package com.PuzzleU.Server.dto.competition;

import com.PuzzleU.Server.entity.enumSet.CompetitionType;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompetitionHomePageDto {
    @Size(max = 100)
    private String competitionName;
    private Integer CompetitionVisit;
    private Integer CompetitionMatching;
    private List<CompetitionType> competitionTypeList;
    private Integer CompetitionDday;
    private LocalDateTime createdAt;


}

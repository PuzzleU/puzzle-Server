package com.PuzzleU.Server.upload.dto;

import com.PuzzleU.Server.common.enumSet.CompetitionType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Data
@Setter
@AllArgsConstructor
@Builder
public class UploadCompetitionDto {
    private MultipartFile competitionPoster;
    private String competitionName;
    private String competitionUrl;
    private String competitionHost;
    private String competitionAwards;
    private String competitionStart;
    private String competitionEnd;
    private String competitionContent;
    private Integer competitionDday;

    private List<String> competitionTypeList;
}

package com.PuzzleU.Server.major.dto;

import com.PuzzleU.Server.major.dto.MajorSearchDto;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MajorSearchTotalDto {
    private List<MajorSearchDto> MajorList;
    private Long UniversityId;
    private String UniversityName;


    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

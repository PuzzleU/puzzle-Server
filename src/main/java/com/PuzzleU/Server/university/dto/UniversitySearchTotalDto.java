package com.PuzzleU.Server.university.dto;

import com.PuzzleU.Server.common.enumSet.UniversityType;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversitySearchTotalDto {
    private List<UniversitySearchDto> UniversityList;
    private UniversityType UniversityType;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

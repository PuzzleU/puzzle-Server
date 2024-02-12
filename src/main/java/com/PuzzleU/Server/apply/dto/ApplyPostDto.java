package com.PuzzleU.Server.apply.dto;

import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApplyPostDto {
    private String ApplyTitle;
    private List<Long> ApplyPositionIdList;
    private String ApplyContent;
}

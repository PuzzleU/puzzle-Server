package com.PuzzleU.Server.interest.dto;

import com.PuzzleU.Server.common.enumSet.InterestTypes;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestTypeDto {
    private InterestTypes InterestType;
    private List<InterestDto> InterestList;
}

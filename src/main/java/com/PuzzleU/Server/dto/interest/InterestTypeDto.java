package com.PuzzleU.Server.dto.interest;

import com.PuzzleU.Server.entity.enumSet.InterestTypes;
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

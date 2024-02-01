package com.PuzzleU.Server.dto.interest;

import com.PuzzleU.Server.entity.enumSet.InterestTypes;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterestDto {
    private Long InterestId;
    private String InterestName;
    private InterestTypes InterestType;
}

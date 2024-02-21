package com.PuzzleU.Server.team.dto;

import com.PuzzleU.Server.apply.dto.UserApplyDto;
import lombok.*;

import java.util.List;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamApplyListDto {
    private SimpleTeamDto Team;
    private List<UserApplyDto> ApplyList;
}

package com.PuzzleU.Server.dto.team;

import com.PuzzleU.Server.entity.competition.Competition;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.boot.context.properties.bind.DefaultValue;

public class TeamCreateDto {
    private Competition competition;
    @Size(max = 30)
    private String teamTitle;

    private Integer teamMemberNow;

    private Integer teamMemberNeed;

    private Boolean teamUntact;

    @Size(max = 200)
    private String teamUrl;

    @Size(max = 300)
    private String teamIntroduce;

    @Size(max = 500)
    private String teamContent;

    private Boolean teamStatus;





}


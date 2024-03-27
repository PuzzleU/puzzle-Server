package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.TermType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TermDto {
    private String termAlias;
    private TermType termType;
    private String termContent ;
}

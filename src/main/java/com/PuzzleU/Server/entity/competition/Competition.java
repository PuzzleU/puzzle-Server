package com.PuzzleU.Server.entity.competition;

import com.PuzzleU.Server.entity.BaseEntity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
public class Competition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CompetitionId;

    @Column(name = "CompetitionName", nullable = true)
    public String CompetitonName;

    @Column(name =  "CompetitionUrl",nullable = true)
    public String CompetitionUrl;

    @Column(name = "CompetitionHost", nullable = true)
    public String CompetitionHost;

    @Column(name = "CompetitionPoster", nullable = true)
    public String CompetitionPoster;

    @Column(name = "CompetitionAwards", nullable = true)
    public String CompetitionAwards;

    @Column(name = "CompetitionStart", nullable = true)
    public Date CompetitionStart;

    @Column(name = "CompetitionEnd", nullable = true)
    public Date CompetitionEnd;

    @Column(name = "CompetitionContent", nullable = true)
    public String CompetitionContent;

    @Column(name = "CompetitionVisit", nullable = true)
    public Integer CompetitionVisit;

    @Column(name = "CompetitionLike", nullable = true)
    public Integer CompetitionLike;

    @Column(name = "CompetitionMatching", nullable = true)
    public Integer CompetitionMatching;




}

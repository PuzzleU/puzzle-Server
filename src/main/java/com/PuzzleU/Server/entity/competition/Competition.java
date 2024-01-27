package com.PuzzleU.Server.entity.competition;

import com.PuzzleU.Server.entity.BaseEntity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
// Competition Entity 생성
@Entity(name = "competitons")
public class Competition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CompetitionId;

    @Column(name = "CompetitionName", nullable = true, length = 100)
    private String CompetitonName;

    @Column(name =  "CompetitionUrl",nullable = true, length = 200)
    private String CompetitionUrl;

    @Column(name = "CompetitionHost", nullable = true, length = 50)
    private String CompetitionHost;

    @Column(name = "CompetitionPoster", nullable = true, length = 200)
    private String CompetitionPoster;

    @Column(name = "CompetitionAwards", nullable = true, length = 50)
    private String CompetitionAwards;

    @Column(name = "CompetitionStart", nullable = true)
    private Date CompetitionStart;

    @Column(name = "CompetitionEnd", nullable = true)
    private Date CompetitionEnd;

    @Column(name = "CompetitionContent", nullable = true, length = 2000)
    private String CompetitionContent;

    @Column(name = "CompetitionVisit", nullable = true)
    private Integer CompetitionVisit;

    @Column(name = "CompetitionLike", nullable = true)
    private Integer CompetitionLike;

    @Column(name = "CompetitionMatching", nullable = true)
    private Integer CompetitionMatching;




}
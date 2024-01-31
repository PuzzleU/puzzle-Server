package com.PuzzleU.Server.entity.competition;

import com.PuzzleU.Server.entity.BaseEntity;
import com.PuzzleU.Server.entity.heart.Heart;
import com.PuzzleU.Server.entity.interest.Interest;
import com.PuzzleU.Server.entity.team.Team;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Competition Entity 생성
@Entity
@Table(name = "competition")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Competition extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long CompetitionId;

    @Column(name = "Competition_name", nullable = true, length = 100)
    private String CompetitionName;

    @Column(name =  "Competition_url",nullable = true, length = 200)
    private String CompetitionUrl;

    @Column(name = "Competition_host", nullable = true, length = 50)
    private String CompetitionHost;

    @Column(name = "Competition_poster", nullable = true, length = 200)
    private String CompetitionPoster;

    @Column(name = "Competition_awards", nullable = true, length = 50)
    private String CompetitionAwards;

    @Column(name = "Competition_start", nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date CompetitionStart;

    @Column(name = "Competition_end", nullable = true)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date CompetitionEnd;

    @Column(name = "Competition_content", nullable = true, length = 2000)
    private String CompetitionContent;

    @Column(name = "Competition_visit", nullable = true)
    private Integer CompetitionVisit;

    @Column(name = "Competition_like", nullable = true)
    private Integer CompetitionLike;

    @Column(name = "Competition_matching", nullable = true)
    private Integer CompetitionMatching;


    @OneToMany(mappedBy = "competition",cascade = CascadeType.ALL)
    private List<Team> team = new ArrayList<>();

    @OneToMany(mappedBy = "competition",cascade = CascadeType.ALL)
    private List<Heart> heart = new ArrayList<>();

    @OneToMany(mappedBy = "competition",cascade = CascadeType.ALL)
    private List<Interest> interest = new ArrayList<>();

}

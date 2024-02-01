package com.PuzzleU.Server.entity.interest;

import com.PuzzleU.Server.entity.competition.Competition;
import com.PuzzleU.Server.entity.enumSet.InterestTypes;
import com.PuzzleU.Server.entity.relations.UserInterestRelation;
import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interest")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Interest {
    @Id
    //E
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interestId;

    @Column(name = "interest_name", length = 10)
    private String interestName;

    @Column(name = "interest_type")
    @Enumerated(value = EnumType.STRING)
    private InterestTypes interestType;

    @OneToMany(mappedBy = "interest", cascade = CascadeType.ALL)
    private List<UserInterestRelation> userInterestRelation = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;
}

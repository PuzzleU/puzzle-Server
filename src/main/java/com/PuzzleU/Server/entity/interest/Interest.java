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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InterestId;

    @Column(name = "interest_name", length = 10)
    private String InterestName;

    @Column(name = "interest_type")
    @Enumerated(value = EnumType.STRING)
    private InterestTypes InterestType;

    @OneToMany(mappedBy = "Interest", cascade = CascadeType.ALL)
    private List<UserInterestRelation> userInterestRelation = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;
}

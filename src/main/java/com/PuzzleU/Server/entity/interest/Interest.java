package com.PuzzleU.Server.entity.interest;

import com.PuzzleU.Server.entity.enumSet.InterestTypes;
import jakarta.persistence.*;

@Entity
@Table(name = "interest")
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InterestId;

    @Column(name = "InterestName")
    private String InterestName;

    @Column(name = "InterestType")
    private InterestTypes InterestType;
}

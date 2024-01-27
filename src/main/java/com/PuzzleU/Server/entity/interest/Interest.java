package com.PuzzleU.Server.entity.interest;

import com.PuzzleU.Server.entity.enumSet.InterestTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "interest")
@Getter
@Setter
public class Interest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InterestId;

    @Column(name = "InterestName")
    private String InterestName;

    @Column(name = "InterestType")
    private InterestTypes InterestType;
}

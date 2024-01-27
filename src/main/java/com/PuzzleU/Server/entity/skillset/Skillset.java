package com.PuzzleU.Server.entity.skillset;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skillset")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Skillset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long SkillsetId;

    private String SkillsetLogo;
}

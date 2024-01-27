package com.PuzzleU.Server.entity.skillset;

import com.PuzzleU.Server.entity.relations.UserSkillsetRelation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "skillset")
    private List<UserSkillsetRelation> userSkillsetRelation = new ArrayList<>();
}

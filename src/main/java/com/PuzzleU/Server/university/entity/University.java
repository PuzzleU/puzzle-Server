package com.PuzzleU.Server.university.entity;

import com.PuzzleU.Server.common.enumSet.UniversityType;
import com.PuzzleU.Server.major.entity.Major;
import com.PuzzleU.Server.relations.entity.UserUniversityRelation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "university")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long universityId;

    @Enumerated(value = EnumType.STRING)
    private UniversityType universityType;

    @Column(length = 15)
    private String universityName;

    @JsonIgnore
    @OneToMany(mappedBy = "university",cascade = CascadeType.REMOVE)
    private List<Major> major = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "university", cascade = CascadeType.REMOVE)
    private List<UserUniversityRelation> userUniversities = new ArrayList<>();
}

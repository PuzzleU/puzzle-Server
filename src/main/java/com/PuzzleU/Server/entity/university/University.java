package com.PuzzleU.Server.entity.university;

import com.PuzzleU.Server.entity.enumSet.UniversityType;
import com.PuzzleU.Server.entity.major.Major;
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
    private Long UniversityId;

    private UniversityType UniversityType;

    private String UniversityName;

    @OneToMany(mappedBy = "University",cascade = CascadeType.ALL)
    private List<Major> major = new ArrayList<>();
}

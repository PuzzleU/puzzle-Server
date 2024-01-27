package com.PuzzleU.Server.entity.university;

import com.PuzzleU.Server.entity.enumSet.UniversityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}

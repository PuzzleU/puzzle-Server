package com.PuzzleU.Server.entity.major;

import com.PuzzleU.Server.entity.position.Position;
import com.PuzzleU.Server.entity.university.University;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "major")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MajorId;

    private String MajorName; // 전공 이름

    // 의존 관계 매핑 (University)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UniversityId")
    private University University;
}

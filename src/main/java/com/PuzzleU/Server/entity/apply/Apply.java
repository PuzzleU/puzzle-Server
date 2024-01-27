package com.PuzzleU.Server.entity.apply;

import com.PuzzleU.Server.entity.BaseEntity;
import com.PuzzleU.Server.entity.enumSet.ApplyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "apply")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Apply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ApplyId;

    private String ApplyTitle; // 지원서 제목
    private String ApplyContent; // 지원서 내용
    private ApplyStatus ApplyStatus; // 지원 상태 (대기/완료/취소)
}

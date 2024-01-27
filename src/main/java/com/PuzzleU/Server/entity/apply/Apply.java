package com.PuzzleU.Server.entity.apply;

import com.PuzzleU.Server.entity.enumSet.ApplyStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "apply")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Apply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applyId;

    private String applyTitle; // 지원서 제목
    private String applyContent; // 지원서 내용
    private ApplyStatus applyStatus; // 지원 상태 (대기/완료/취소)
}

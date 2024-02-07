package com.PuzzleU.Server.repository;

import com.PuzzleU.Server.entity.competition.Competition;
import com.PuzzleU.Server.entity.enumSet.CompetitionType;
import feign.Param;
import io.micrometer.common.lang.NonNullApi;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@NonNullApi
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Page<Competition> findAll(@NonNull Pageable pageable);
    Page<Competition> findByCompetitionNameContaining(Pageable pageable,String keyword);


    @Query("SELECT c FROM Competition c LEFT JOIN FETCH c.competitionTypes WHERE c.competitionId = :id")
    Optional<Competition> findByCompetitionIdWithCompetitionTypes(@Param("id") Long id);

    @Query("SELECT c FROM Competition c LEFT JOIN FETCH c.competitionTypes WHERE c.competitionName LIKE %:keyword% AND :type MEMBER OF c.competitionTypes")
    List<Competition> findByKeywordAndType(@Param("keyword") String keyword, @Param("type") CompetitionType type);

    @Query("SELECT c FROM Competition c LEFT JOIN FETCH c.competitionTypes WHERE :type MEMBER OF c.competitionTypes")
    List<Competition> findByType(@Param("type") CompetitionType type, Pageable pageable);

    @Query("SELECT c FROM Competition c LEFT JOIN FETCH c.competitionTypes WHERE c.competitionName LIKE %:keyword%")
    List<Competition> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Modifying
    @Query("update Competition c set c.competitionVisit = c.competitionVisit +1 where c.competitionId = :id")
    int updateVisit(@Param("id")Long id);

    @Query("SELECT c FROM Competition c WHERE c.competitionName LIKE %:keyword%")
    List<Competition> findByCompetitionName(@Param("keyword") String keyword, Pageable pageable);
}


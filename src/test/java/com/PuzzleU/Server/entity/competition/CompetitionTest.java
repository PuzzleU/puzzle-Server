package com.PuzzleU.Server;

import com.PuzzleU.Server.entity.competition.Competition;
import com.PuzzleU.Server.repository.CompetitionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class CompetitionTest {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Test
    public void createAndSaveCompetition() {
        Competition competition = new Competition();
        competition.setCompetitionName("새로운 대회");
        competition.setCompetitionUrl("http://example.com");
        competition.setCompetitionHost("주최자");
        competition.setCompetitionPoster("http://example.com/poster.jpg");
        competition.setCompetitionAwards("대상 1억원");
        competition.setCompetitionStart(new Date()); // 현재 날짜를 시작 날짜로 설정
        competition.setCompetitionEnd(new Date()); // 현재 날짜를 종료 날짜로 설정
        competition.setCompetitionContent("대회 내용 및 상세 정보");
        competition.setCompetitionVisit(0);
        competition.setCompetitionLike(0);
        competition.setCompetitionMatching(0);

        competitionRepository.save(competition); // 대회 정보를 데이터베이스에 저장
    }
}
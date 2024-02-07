package com.PuzzleU.Server.interest.service;

import com.PuzzleU.Server.interest.dto.InterestDto;
import com.PuzzleU.Server.interest.dto.InterestTypeDto;
import com.PuzzleU.Server.interest.entity.Interest;
import com.PuzzleU.Server.interest.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.PuzzleU.Server.common.enumSet.InterestTypes.*;

@Service
@RequiredArgsConstructor
public class InterestService {
    private final InterestRepository interestRepository;

    // interest list: 전체 interest list 반환 (공모전/직무/스터디로 구분)
    public List<InterestTypeDto> listAllInterest() {

        // interest list 받아오기
        List<Interest> competitionInterestsList = interestRepository.findByInterestType(Competition); // 공모전 interest
        List<Interest> jobInterestList = interestRepository.findByInterestType(Job); // 직무 interest
        List<Interest> studyInterestList = interestRepository.findByInterestType(Study); // 스터디 interest

        // dto list로 변환
        List<InterestDto> competitionInterestDtoList = competitionInterestsList.stream()
                .map(interest -> InterestDto.builder()
                        .InterestId(interest.getInterestId())
                        .InterestName(interest.getInterestName())
                        .build())
                .collect(Collectors.toList());
        List<InterestDto> jobInterestDtoList = jobInterestList.stream()
                .map(interest -> InterestDto.builder()
                        .InterestId(interest.getInterestId())
                        .InterestName(interest.getInterestName())
                        .build())
                .collect(Collectors.toList());
        List<InterestDto> studyInterestDtoList = studyInterestList.stream()
                .map(interest -> InterestDto.builder()
                        .InterestId(interest.getInterestId())
                        .InterestName(interest.getInterestName())
                        .build())
                .collect(Collectors.toList());


        // InterestTypeDto로 변환 후 하나의 list로 합치기
        List<InterestTypeDto> interestTypeDtoList = Arrays.asList(
                InterestTypeDto.builder().InterestType(Competition).InterestList(competitionInterestDtoList).build(),
                InterestTypeDto.builder().InterestType(Job).InterestList(jobInterestDtoList).build(),
                InterestTypeDto.builder().InterestType(Study).InterestList(studyInterestDtoList).build()
        );

        return interestTypeDtoList;
    }

}

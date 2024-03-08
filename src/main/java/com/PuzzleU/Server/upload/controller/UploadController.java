package com.PuzzleU.Server.upload.controller;

import com.PuzzleU.Server.common.enumSet.CompetitionType;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.competition.repository.CompetitionRepository;
import com.PuzzleU.Server.profile.entity.Profile;
import com.PuzzleU.Server.profile.repository.ProfileRepository;
import com.PuzzleU.Server.skillset.entity.Skillset;
import com.PuzzleU.Server.skillset.repository.SkillsetRepository;
import com.PuzzleU.Server.upload.dto.UploadCompetitionDto;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class UploadController {

    private final AmazonS3Client amazonS3Client;
    private final ProfileRepository profileRepository;
    private final SkillsetRepository skillsetRepository;
    private final CompetitionRepository competitionRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostMapping("/profile")
    public ResponseEntity<String> uploadProfile(@RequestParam("profileImage") MultipartFile file) {
        try {
            String fileName =file.getOriginalFilename();
            String folder = "/profile"; // 저장할 폴더

            String fileUrl= "https://" + bucket + ".s3." + region + ".amazonaws.com" + folder + "/" + fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket + folder, fileName, file.getInputStream(), metadata);

            Profile profile = new Profile();
            profile.setProfielUrl(fileUrl);

            profileRepository.save(profile);

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/skillset")
    public ResponseEntity<String> uploadSkillset(@RequestParam("skillsetLogo") MultipartFile file, @RequestParam("skillsetName") String skillsetName) {

        try {
            String fileName =file.getOriginalFilename();
            String folder = "/skillset"; // 저장할 폴더

            String fileUrl= "https://" + bucket + ".s3." + region + ".amazonaws.com" + folder + "/" + fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket + folder, fileName, file.getInputStream(), metadata);

            Skillset skillset = new Skillset();
            skillset.setSkillsetLogo(fileUrl);
            skillset.setSkillsetName(skillsetName);

            skillsetRepository.save(skillset);

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/competition")
    public ResponseEntity<String> uploadCompetition (
            @ModelAttribute UploadCompetitionDto uploadCompetitionDto
            ) {
        try {
            MultipartFile file = uploadCompetitionDto.getCompetitionPoster();

            String fileName =file.getOriginalFilename();
            String folder = "/competition"; // 저장할 폴더

            String fileUrl= "https://" + bucket + ".s3." + region + ".amazonaws.com" + folder + "/" + fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket + folder, fileName, file.getInputStream(), metadata);

            // 날짜 형식: yyyy-MM-dd HH:mm
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            Competition competition = new Competition();

            competition.setCompetitionName(uploadCompetitionDto.getCompetitionName());
            competition.setCompetitionPoster(fileUrl);
            competition.setCompetitionUrl(uploadCompetitionDto.getCompetitionUrl());
            competition.setCompetitionHost(uploadCompetitionDto.getCompetitionHost());
            competition.setCompetitionAwards(uploadCompetitionDto.getCompetitionAwards());
            competition.setCompetitionStart(LocalDateTime.parse(uploadCompetitionDto.getCompetitionStart(), formatter));
            competition.setCompetitionEnd(LocalDateTime.parse(uploadCompetitionDto.getCompetitionEnd(), formatter));
            competition.setCompetitionContent(uploadCompetitionDto.getCompetitionContent());
            competition.setCompetitionVisit(0);
            competition.setCompetitionLike(0);
            competition.setCompetitionMatching(0);
            competition.setCompetitionDDay(uploadCompetitionDto.getCompetitionDday());

            List<CompetitionType> competitionTypeList = new ArrayList<>();
            for (String competitionType : uploadCompetitionDto.getCompetitionTypeList()) {
                if (competitionType.equals("PLAN")) {
                    competitionTypeList.add(CompetitionType.PLAN);
                }
                else if (competitionType.equals("DESIGN")) {
                    competitionTypeList.add(CompetitionType.DESIGN);
                }
                else if (competitionType.equals("LITURE")) {
                    competitionTypeList.add(CompetitionType.LITURE);
                }
                else if (competitionType.equals("MEDIA")) {
                    competitionTypeList.add(CompetitionType.MEDIA);
                }
                else if (competitionType.equals("MARKET")) {
                    competitionTypeList.add(CompetitionType.MARKET);
                }
                else if (competitionType.equals("NAME")) {
                    competitionTypeList.add(CompetitionType.NAME);
                }
                else if (competitionType.equals("ART")) {
                    competitionTypeList.add(CompetitionType.ART);
                }
                else if (competitionType.equals("IT")) {
                    competitionTypeList.add(CompetitionType.IT);
                }
                else if (competitionType.equals("START")) {
                    competitionTypeList.add(CompetitionType.START);
                }
                else if (competitionType.equals("ETC")) {
                    competitionTypeList.add(CompetitionType.ETC);
                }
            }

            competition.setCompetitionTypes(competitionTypeList);

            competitionRepository.save(competition);

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

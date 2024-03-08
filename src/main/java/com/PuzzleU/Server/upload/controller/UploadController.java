package com.PuzzleU.Server.upload.controller;

import com.PuzzleU.Server.common.enumSet.CompetitionType;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.competition.repository.CompetitionRepository;
import com.PuzzleU.Server.profile.entity.Profile;
import com.PuzzleU.Server.profile.repository.ProfileRepository;
import com.PuzzleU.Server.skillset.entity.Skillset;
import com.PuzzleU.Server.skillset.repository.SkillsetRepository;
import com.PuzzleU.Server.upload.dto.UploadCompetitionDto;
import com.PuzzleU.Server.upload.service.UploadService;
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

    private final UploadService uploadService;

    @PostMapping("/profile")
    public ResponseEntity<String> uploadProfile(@RequestParam("profileImage") MultipartFile file) {
        return uploadService.uploadProfile(file);
    }

    @PostMapping("/skillset")
    public ResponseEntity<String> uploadSkillset(@RequestParam("skillsetLogo") MultipartFile file, @RequestParam("skillsetName") String skillsetName) {
        return uploadService.uploadSkillset(file, skillsetName);
    }

    @PostMapping("/competition")
    public ResponseEntity<String> uploadCompetition (@ModelAttribute UploadCompetitionDto uploadCompetitionDto) {
       return uploadService.uploadCompetition(uploadCompetitionDto);
    }

}

package com.PuzzleU.Server.upload.controller;

import com.PuzzleU.Server.profile.entity.Profile;
import com.PuzzleU.Server.profile.repository.ProfileRepository;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class UploadController {

    private final AmazonS3Client amazonS3Client;
    private final ProfileRepository profileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostMapping("/profile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
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

}

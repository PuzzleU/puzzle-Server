package com.PuzzleU.Server.entity.university;

import com.PuzzleU.Server.repository.UniversityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UniversityTest {
    @Autowired
    private UniversityRepository universityRepository;

    @Test
    public void testDeleteUniversity()
    {
        Long universityId = 1L;
        universityRepository.deleteById(universityId);
    }

}
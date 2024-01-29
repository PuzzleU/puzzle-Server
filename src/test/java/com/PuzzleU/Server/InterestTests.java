package com.PuzzleU.Server;

import com.PuzzleU.Server.entity.enumSet.InterestTypes;
import com.PuzzleU.Server.entity.interest.Interest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InterestTests {

    @Test
    public void testInterestAttributes() {
        // Given
        String interestName = "TestInterest";
        InterestTypes interestType = InterestTypes.Competition; // Replace with the actual interest type enum value

        // When
        Interest interest = Interest.builder()
                .InterestName(interestName)
                .InterestType(interestType)
                .build();

        // Then
        assertAll(
                () -> assertNotNull(interest, "Interest should not be null"),
                () -> assertNull(interest.getInterestId(), "InterestId should be null before persisting"),
                () -> assertEquals(interestName, interest.getInterestName(), "InterestName should match"),
                () -> assertEquals(interestType, interest.getInterestType(), "InterestType should match")

        );
    }
}

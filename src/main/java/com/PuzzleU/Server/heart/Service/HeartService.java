package com.PuzzleU.Server.heart.Service;

import com.PuzzleU.Server.common.api.ApiResponseDto;
import com.PuzzleU.Server.common.api.ResponseUtils;
import com.PuzzleU.Server.common.api.SuccessResponse;
import com.PuzzleU.Server.common.enumSet.ErrorType;
import com.PuzzleU.Server.common.exception.RestApiException;
import com.PuzzleU.Server.competition.entity.Competition;
import com.PuzzleU.Server.competition.repository.CompetitionRepository;
import com.PuzzleU.Server.heart.entity.Heart;
import com.PuzzleU.Server.heart.repository.HeartRepository;
import com.PuzzleU.Server.user.entity.User;
import com.PuzzleU.Server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HeartService {

    private final UserRepository userRepository;
    private final CompetitionRepository competitionRepository;
    private final HeartRepository heartRepository;

    @Transactional
    public ApiResponseDto<SuccessResponse> heartCreate(UserDetails loginuser, Long competitionId)
    {
        User user = userRepository.findByUsername(loginuser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        Competition competition = competitionOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND_COMPETITION)
        );
        List<Heart> hearts = heartRepository.findByCompetitionAndUser(competition,user);
        List<Heart> user_heart = heartRepository.findByCompetitionAndUserNot(competition,user);
        int heart = hearts.size();
        if (user_heart.size()==0)
        {
            heart +=1;
            Heart heart_user = new Heart();
            heart_user.setCompetition(competition);
            heart_user.setUser(user);
            heartRepository.save(heart_user);
        }
        else
        {
            throw new RestApiException(ErrorType.FOUND_LIKE);
        }
        competition.setCompetitionLike(heart);
        competitionRepository.save(competition);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "좋아요를 눌렀습니다"), null);
    }
    @Transactional
    public ApiResponseDto<SuccessResponse> heartDelete(UserDetails loginuser, Long competitionId)
    {
        User user = userRepository.findByUsername(loginuser.getUsername())
                .orElseThrow(() -> new RestApiException(ErrorType.NOT_FOUND_USER));
        Optional<Competition> competitionOptional = competitionRepository.findById(competitionId);
        Competition competition = competitionOptional.orElseThrow(
                () -> new RestApiException(ErrorType.NOT_FOUND_COMPETITION)
        );
        List<Heart> hearts = heartRepository.findByCompetitionAndUser(competition,user);
        Heart user_heart = heartRepository.findOneByCompetitionAndUserNot(competition,user);
        int heart = hearts.size();
        heart -=1;
        heartRepository.delete(user_heart);
        competition.setCompetitionLike(heart);
        competitionRepository.save(competition);
        return ResponseUtils.ok(SuccessResponse.of(HttpStatus.OK, "좋아요를 취소하였습니다"), null);
    }
}

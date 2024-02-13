package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.FriendStatus;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto extends UserMyProfileDto {
    private FriendStatus FriendStatus;
}

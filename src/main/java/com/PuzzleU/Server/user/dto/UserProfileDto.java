package com.PuzzleU.Server.user.dto;

import com.PuzzleU.Server.common.enumSet.FriendStatus;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto extends UserMyProfileDto {
    private FriendStatus FriendStatus;
}

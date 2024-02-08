package com.PuzzleU.Server.friendship.dto;

import com.PuzzleU.Server.user.entity.User;
import lombok.*;

@Getter
@Data
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FriendShipRegisterDto {
    private com.PuzzleU.Server.user.entity.User user1;
    private User user2;
    private Boolean status;
}

package be.zsoft.zscore.core.security.dto;

import be.zsoft.zscore.core.entity.game.Game;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.player.Player;
import be.zsoft.zscore.core.entity.user.User;

public record AuthenticationData(
        User user,
        Player player,
        Game game,
        Organization organization
) {
}

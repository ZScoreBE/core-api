package be.zsoft.zscore.core.controller.user;

import be.zsoft.zscore.core.dto.mapper.user.UserInviteMapper;
import be.zsoft.zscore.core.dto.response.user.UserInviteResponse;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.service.user.UserInviteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public/users/invites")
public class PublicUserInviteController {

    private final UserInviteService userInviteService;
    private final UserInviteMapper userInviteMapper;

    @GetMapping("/by-code/{code}")
    public UserInviteResponse getByCode(@PathVariable  UUID code) {
        UserInvite invite = userInviteService.getInvite(code);

        return userInviteMapper.toResponse(invite);
    }
}

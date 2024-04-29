package be.zsoft.zscore.core.controller.user;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.user.UserInviteMapper;
import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import be.zsoft.zscore.core.dto.response.user.UserInviteResponse;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.service.user.UserInviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/invites")
public class UserInviteController {

    private final UserInviteMapper userInviteMapper;
    private final UserInviteService userInviteService;

    @Secured({"ROLE_ORGANIZATION_ADMIN"})
    @PostMapping
    public UserInviteResponse createUserInvite(@RequestBody @Valid UserInviteRequest request) {
        UserInvite invite = userInviteService.createInvite(request);
        return userInviteMapper.toResponse(invite);
    }

    @Secured("ROLE_ORGANIZATION_ADMIN")
    @GetMapping("/pending")
    public PaginatedResponse<UserInviteResponse> getPendingInvites(
            @RequestParam(name = "search", required = false) String search,
            Pageable pageable
    ) {
        Page<UserInvite> invites = StringUtils.hasText(search) ?
                userInviteService.searchPendingInvites(search, pageable) :
                userInviteService.getPendingInvites(pageable);
        return PaginatedResponse.createResponse(userInviteMapper.toResponse(invites), "/users/invite/pending");
    }

    @Secured("ROLE_ORGANIZATION_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvite(@PathVariable UUID id) {
        userInviteService.deleteInvite(id);
    }

}

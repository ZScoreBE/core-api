package be.zsoft.zscore.core.controller.user;

import be.zsoft.zscore.core.common.pagination.PaginatedResponse;
import be.zsoft.zscore.core.dto.mapper.user.UserInviteMapper;
import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import be.zsoft.zscore.core.dto.response.user.UserInviteResponse;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.entity.user.UserInviteStatus;
import be.zsoft.zscore.core.service.user.UserInviteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInviteControllerTest {

    @Mock
    private UserInviteMapper userInviteMapper;

    @Mock
    private UserInviteService userInviteService;

    @InjectMocks
    private UserInviteController userInviteController;

    @Test
    void createUserInvite() {
        UserInviteRequest request = new UserInviteRequest("wout@z-soft.be", "wout");
        UserInvite invite = UserInvite.builder().id(UUID.randomUUID()).build();
        UserInviteResponse expected = new UserInviteResponse(UUID.randomUUID(), "wout@z-soft.be", "wout", UserInviteStatus.PENDING);

        when(userInviteService.createInvite(request)).thenReturn(invite);
        when(userInviteMapper.toResponse(invite)).thenReturn(expected);

        UserInviteResponse result = userInviteController.createUserInvite(request);

        assertEquals(expected, result);

        verify(userInviteService).createInvite(request);
        verify(userInviteMapper).toResponse(invite);
    }

    @Test
    void getPendingInvites() {
        Page<UserInvite> invites = new PageImpl<>(List.of(
                UserInvite.builder().id(UUID.randomUUID()).build(),
                UserInvite.builder().id(UUID.randomUUID()).build()
        ));
        Page<UserInviteResponse> expected = new PageImpl<>(List.of(
                new UserInviteResponse(UUID.randomUUID(), "wout@-zoft.be", "wout", UserInviteStatus.PENDING),
                new UserInviteResponse(UUID.randomUUID(), "wout@-zoft.be", "wout", UserInviteStatus.PENDING)
        ));
        Pageable pageable = PageRequest.of(1, 10);

        when(userInviteService.getPendingInvites(pageable)).thenReturn(invites);
        when(userInviteMapper.toResponse(invites)).thenReturn(expected);

        PaginatedResponse<UserInviteResponse> result = userInviteController.getPendingInvites(null, pageable);

        assertEquals(expected.getContent(), result.items());

        verify(userInviteService).getPendingInvites(pageable);
        verify(userInviteService, never()).searchPendingInvites(anyString(), eq(pageable));
        verify(userInviteMapper).toResponse(invites);
    }

    @Test
    void getPendingInvites_search() {
        Page<UserInvite> invites = new PageImpl<>(List.of(
                UserInvite.builder().id(UUID.randomUUID()).build(),
                UserInvite.builder().id(UUID.randomUUID()).build()
        ));
        Page<UserInviteResponse> expected = new PageImpl<>(List.of(
                new UserInviteResponse(UUID.randomUUID(), "wout@-zoft.be", "wout", UserInviteStatus.PENDING),
                new UserInviteResponse(UUID.randomUUID(), "wout@-zoft.be", "wout", UserInviteStatus.PENDING)
        ));
        Pageable pageable = PageRequest.of(1, 10);

        when(userInviteService.searchPendingInvites("test", pageable)).thenReturn(invites);
        when(userInviteMapper.toResponse(invites)).thenReturn(expected);

        PaginatedResponse<UserInviteResponse> result = userInviteController.getPendingInvites("test", pageable);

        assertEquals(expected.getContent(), result.items());

        verify(userInviteService, never()).getPendingInvites(pageable);
        verify(userInviteService).searchPendingInvites("test", pageable);
        verify(userInviteMapper).toResponse(invites);
    }

    @Test
    void deleteInvite() {
        UUID id = UUID.randomUUID();

        userInviteController.deleteInvite(id);

        verify(userInviteService).deleteInvite(id);
    }
}
package be.zsoft.zscore.core.controller.user;

import be.zsoft.zscore.core.dto.mapper.user.UserInviteMapper;
import be.zsoft.zscore.core.dto.response.user.UserInviteResponse;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.fixtures.user.UserInviteFixture;
import be.zsoft.zscore.core.fixtures.user.UserInviteResponseFixture;
import be.zsoft.zscore.core.service.user.UserInviteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublicUserInviteControllerTest {

    @Mock
    private UserInviteService userInviteService;

    @Mock
    private UserInviteMapper userInviteMapper;

    @InjectMocks
    private PublicUserInviteController controller;

    @Test
    void getByCode() {
        UUID code = UUID.randomUUID();
        UserInvite invite = UserInviteFixture.aDefaultInvite();
        UserInviteResponse expected = UserInviteResponseFixture.aDefaultUserInviteResponse();

        when(userInviteService.getInvite(code)).thenReturn(invite);
        when(userInviteMapper.toResponse(invite)).thenReturn(expected);

        UserInviteResponse result = controller.getByCode(code);

        assertEquals(expected, result);

        verify(userInviteService).getInvite(code);
        verify(userInviteMapper).toResponse(invite);
    }
}
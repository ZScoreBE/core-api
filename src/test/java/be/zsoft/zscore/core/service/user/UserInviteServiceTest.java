package be.zsoft.zscore.core.service.user;

import be.zsoft.zscore.core.common.exception.NotFoundException;
import be.zsoft.zscore.core.dto.mapper.user.UserInviteMapper;
import be.zsoft.zscore.core.dto.request.user.UserInviteRequest;
import be.zsoft.zscore.core.entity.organization.Organization;
import be.zsoft.zscore.core.entity.user.UserInvite;
import be.zsoft.zscore.core.entity.user.UserInviteStatus;
import be.zsoft.zscore.core.repository.user.UserInviteRepo;
import be.zsoft.zscore.core.service.organization.OrganizationService;
import be.zsoft.zscore.core.service.utils.SMTPEmailService;
import be.zsoft.zscore.core.validation.request.UserInviteValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInviteServiceTest {

    @Mock
    private UserInviteMapper userInviteMapper;

    @Mock
    private UserInviteRepo userInviteRepo;

    @Mock
    private UserInviteValidator userInviteValidator;

    @Mock
    private OrganizationService organizationService;

    @Mock
    private SMTPEmailService emailService;

    @InjectMocks
    private UserInviteService userInviteService;

    @Captor
    private ArgumentCaptor<UserInvite> userInviteArgumentCaptor;

    @Test
    void createInvite() {
        UserInviteRequest request = new UserInviteRequest("wout@z-soft.be", "wout");
        UserInvite expected = UserInvite.builder().id(UUID.randomUUID()).build();
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();

        when(userInviteMapper.fromRequest(request)).thenReturn(expected);
        when(userInviteRepo.saveAndFlush(expected)).thenReturn(expected);
        when(organizationService.getMyOrganization()).thenReturn(organization);

        UserInvite result = userInviteService.createInvite(request);

        assertEquals(expected, result);

        verify(userInviteValidator).validate(request);
        verify(userInviteMapper).fromRequest(request);
        verify(organizationService).getMyOrganization();
        verify(emailService).sendInviteEmail(expected);
        verify(userInviteRepo).saveAndFlush(userInviteArgumentCaptor.capture());

        assertNotNull(userInviteArgumentCaptor.getValue().getInviteCode());
        assertEquals(UserInviteStatus.PENDING, userInviteArgumentCaptor.getValue().getStatus());
        assertEquals(organization, userInviteArgumentCaptor.getValue().getOrganization());
    }

    @Test
    void getPendingInvites() {
        Pageable pageable = PageRequest.of(1, 10);
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        Page<UserInvite> expected = new PageImpl<>(List.of(
                UserInvite.builder().id(UUID.randomUUID()).build(),
                UserInvite.builder().id(UUID.randomUUID()).build()
        ));

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(userInviteRepo.findAllByStatusAndOrganization(UserInviteStatus.PENDING, organization, pageable)).thenReturn(expected);


        Page<UserInvite> result = userInviteService.getPendingInvites(pageable);

        assertEquals(expected, result);

        verify(userInviteRepo).findAllByStatusAndOrganization(UserInviteStatus.PENDING, organization, pageable);

    }

    @Test
    void getPendingInvites_search() {
        Pageable pageable = PageRequest.of(1, 10);
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        Page<UserInvite> expected = new PageImpl<>(List.of(
                UserInvite.builder().id(UUID.randomUUID()).build(),
                UserInvite.builder().id(UUID.randomUUID()).build()
        ));

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(userInviteRepo.searchAllOnNameAndEmailByStatusAndOrganization("%test%", UserInviteStatus.PENDING, organization, pageable)).thenReturn(expected);

        Page<UserInvite> result = userInviteService.searchPendingInvites("test", pageable);

        assertEquals(expected, result);

        verify(userInviteRepo).searchAllOnNameAndEmailByStatusAndOrganization("%test%", UserInviteStatus.PENDING, organization, pageable);
    }

    @Test
    void getInvite_success() {
        UUID inviteCode = UUID.randomUUID();
        UserInvite expected = UserInvite.builder().id(UUID.randomUUID()).build();

        when(userInviteRepo.findByInviteCode(inviteCode)).thenReturn(Optional.of(expected));

        UserInvite result = userInviteService.getInvite(inviteCode);

        assertEquals(expected, result);

        verify(userInviteRepo).findByInviteCode(inviteCode);
    }

    @Test
    void getInvite_notFound() {
        UUID inviteCode = UUID.randomUUID();

        when(userInviteRepo.findByInviteCode(inviteCode)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userInviteService.getInvite(inviteCode));

        verify(userInviteRepo).findByInviteCode(inviteCode);
    }

    @Test
    void getInviteWithOrganization_success() {
        UUID inviteCode = UUID.randomUUID();
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();
        UserInvite expected = UserInvite.builder().id(UUID.randomUUID()).build();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(userInviteRepo.findByInviteCodeAndOrganization(inviteCode, organization)).thenReturn(Optional.of(expected));

        UserInvite result = userInviteService.getInviteWithOrganization(inviteCode);

        assertEquals(expected, result);

        verify(organizationService).getMyOrganization();
        verify(userInviteRepo).findByInviteCodeAndOrganization(inviteCode, organization);
    }

    @Test
    void getInviteWithOrganization_notFound() {
        UUID inviteCode = UUID.randomUUID();
        Organization organization = Organization.builder().id(UUID.randomUUID()).build();

        when(organizationService.getMyOrganization()).thenReturn(organization);
        when(userInviteRepo.findByInviteCodeAndOrganization(inviteCode, organization)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userInviteService.getInviteWithOrganization(inviteCode));

        verify(organizationService).getMyOrganization();
        verify(userInviteRepo).findByInviteCodeAndOrganization(inviteCode, organization);
    }

    @Test
    void acceptInvite() {
        UserInvite invite = UserInvite.builder().id(UUID.randomUUID()).build();

        userInviteService.acceptInvite(invite);

        verify(userInviteRepo).saveAndFlush(userInviteArgumentCaptor.capture());

        assertEquals(UserInviteStatus.ACCEPTED, userInviteArgumentCaptor.getValue().getStatus());
    }

    @Test
    void deleteInvite() {
        UUID inviteCode = UUID.randomUUID();

        userInviteService.deleteInvite(inviteCode);

        verify(userInviteRepo).deleteById(inviteCode);
    }
}
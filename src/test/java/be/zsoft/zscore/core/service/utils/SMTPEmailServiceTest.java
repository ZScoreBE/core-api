package be.zsoft.zscore.core.service.utils;

import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.config.properties.MailProperties;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.entity.user.UserInvite;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SMTPEmailServiceTest {

    @Mock
    private MailProperties mailProperties;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PebbleService pebbleService;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private SMTPEmailService emailService;


    @Test
    void sendActivationMail_withClasspathTemplate() throws Exception{
        try(MockedConstruction<MimeMessageHelper> mockMimeMessageHelper = mockConstruction(MimeMessageHelper.class)) {
            User user = User.builder().name("wout").email("wout@z-soft.be").activationCode("code123").build();

            when(mailProperties.isOverrideClasspathTemplates()).thenReturn(false);
            when(mailProperties.getActivationFrontendUrl()).thenReturn("/activate");
            when(mailProperties.getNoReplyEmail()).thenReturn("no-reply@z-soft.be");
            when(mailProperties.getNoReplyName()).thenReturn("no-reply");
            when(pebbleService.generateHTML(anyString(), anyMap())).thenReturn("theHTML");
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendActivationMail(user);

            verify(pebbleService).generateHTML("mail-templates/activate.peb", Map.of(
                    "name", "wout",
                    "url", "/activate?code=code123"
            ));

            MimeMessageHelper messageHelper = mockMimeMessageHelper.constructed().get(0);
            verify(messageHelper).setTo(new InternetAddress("wout@z-soft.be", "wout"));
            verify(messageHelper).setFrom(new InternetAddress("no-reply@z-soft.be","no-reply"));
            verify(messageHelper).setSubject("Activate your account");
            verify(messageHelper).setText("theHTML", true);

            verify(mailSender).send(mimeMessage);
        }
    }

    @Test
    void sendActivationMail_withOverrideTemplate() throws Exception{
        try(MockedConstruction<MimeMessageHelper> mockMimeMessageHelper = mockConstruction(MimeMessageHelper.class)) {
            User user = User.builder().name("wout").email("wout@z-soft.be").activationCode("code123").build();

            when(mailProperties.isOverrideClasspathTemplates()).thenReturn(true);
            when(mailProperties.getAbsoluteEmailTemplatesPath()).thenReturn("/templates");
            when(mailProperties.getActivationFrontendUrl()).thenReturn("/activate");
            when(mailProperties.getNoReplyEmail()).thenReturn("no-reply@z-soft.be");
            when(mailProperties.getNoReplyName()).thenReturn("no-reply");
            when(pebbleService.generateHTML(anyString(), anyMap())).thenReturn("theHTML");
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendActivationMail(user);

            verify(pebbleService).generateHTML("/templates/activate.peb", Map.of(
                    "name", "wout",
                    "url", "/activate?code=code123"
            ));

            MimeMessageHelper messageHelper = mockMimeMessageHelper.constructed().get(0);
            verify(messageHelper).setTo(new InternetAddress("wout@z-soft.be", "wout"));
            verify(messageHelper).setFrom(new InternetAddress("no-reply@z-soft.be","no-reply"));
            verify(messageHelper).setSubject("Activate your account");
            verify(messageHelper).setText("theHTML", true);

            verify(mailSender).send(mimeMessage);
        }
    }

    @Test
    void sendActivationMail_withException() throws Exception {
            User user = User.builder().name("wout").email("wout@z-soft.be").activationCode("code123").build();

            when(mailProperties.isOverrideClasspathTemplates()).thenReturn(true);
            when(mailProperties.getAbsoluteEmailTemplatesPath()).thenReturn("/templates");
            when(mailProperties.getActivationFrontendUrl()).thenReturn("/activate");
            when(pebbleService.generateHTML(anyString(), anyMap())).thenThrow(IOException.class);

            assertThrows(ApiException.class, () -> emailService.sendActivationMail(user));

            verify(pebbleService).generateHTML("/templates/activate.peb", Map.of(
                    "name", "wout",
                    "url", "/activate?code=code123"
            ));
            verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendPasswordResetMail_successSend() throws Exception {
        try(MockedConstruction<MimeMessageHelper> mockMimeMessageHelper = mockConstruction(MimeMessageHelper.class)) {
            User user = User.builder().name("wout").email("wout@z-soft.be").activationCode("code123").build();

            when(mailProperties.isOverrideClasspathTemplates()).thenReturn(true);
            when(mailProperties.getAbsoluteEmailTemplatesPath()).thenReturn("/templates");
            when(mailProperties.getResetPasswordFrontendUrl()).thenReturn("/password-reset");
            when(mailProperties.getNoReplyEmail()).thenReturn("no-reply@z-soft.be");
            when(mailProperties.getNoReplyName()).thenReturn("no-reply");
            when(pebbleService.generateHTML(anyString(), anyMap())).thenReturn("theHTML");
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendPasswordResetMail(user);

            verify(pebbleService).generateHTML("/templates/password-reset.peb", Map.of(
                    "name", "wout",
                    "url", "/password-reset?code=code123"
            ));

            MimeMessageHelper messageHelper = mockMimeMessageHelper.constructed().get(0);
            verify(messageHelper).setTo(new InternetAddress("wout@z-soft.be", "wout"));
            verify(messageHelper).setFrom(new InternetAddress("no-reply@z-soft.be","no-reply"));
            verify(messageHelper).setSubject("Reset your password");
            verify(messageHelper).setText("theHTML", true);

            verify(mailSender).send(mimeMessage);
        }
    }

    @Test
    void sendPasswordResetMail_withException() throws Exception {
        User user = User.builder().name("wout").email("wout@z-soft.be").activationCode("code123").build();

        when(mailProperties.isOverrideClasspathTemplates()).thenReturn(true);
        when(mailProperties.getAbsoluteEmailTemplatesPath()).thenReturn("/templates");
        when(mailProperties.getResetPasswordFrontendUrl()).thenReturn("/reset-password");
        when(pebbleService.generateHTML(anyString(), anyMap())).thenThrow(IOException.class);

        assertThrows(ApiException.class, () -> emailService.sendPasswordResetMail(user));

        verify(pebbleService).generateHTML("/templates/password-reset.peb", Map.of(
                "name", "wout",
                "url", "/reset-password?code=code123"
        ));
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendInviteEmail_successSend() throws Exception {
        try(MockedConstruction<MimeMessageHelper> mockMimeMessageHelper = mockConstruction(MimeMessageHelper.class)) {
            UUID inviteCode = UUID.randomUUID();
            UserInvite invite = UserInvite.builder().name("wout").email("wout@z-soft.be").inviteCode(inviteCode).build();

            when(mailProperties.isOverrideClasspathTemplates()).thenReturn(true);
            when(mailProperties.getAbsoluteEmailTemplatesPath()).thenReturn("/templates");
            when(mailProperties.getRegisterFrontendUrl()).thenReturn("/sign-up");
            when(mailProperties.getNoReplyEmail()).thenReturn("no-reply@z-soft.be");
            when(mailProperties.getNoReplyName()).thenReturn("no-reply");
            when(pebbleService.generateHTML(anyString(), anyMap())).thenReturn("theHTML");
            when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

            emailService.sendInviteEmail(invite);

            verify(pebbleService).generateHTML("/templates/invite.peb", Map.of(
                    "name", "wout",
                    "url", "/sign-up?inviteCode=" + inviteCode
            ));

            MimeMessageHelper messageHelper = mockMimeMessageHelper.constructed().get(0);
            verify(messageHelper).setTo(new InternetAddress("wout@z-soft.be", "wout"));
            verify(messageHelper).setFrom(new InternetAddress("no-reply@z-soft.be","no-reply"));
            verify(messageHelper).setSubject("You have been invited to Z-Score");
            verify(messageHelper).setText("theHTML", true);

            verify(mailSender).send(mimeMessage);
        }
    }

    @Test
    void sendInviteEmail_withException() throws Exception {
        UUID inviteCode = UUID.randomUUID();
        UserInvite invite = UserInvite.builder().name("wout").email("wout@z-soft.be").inviteCode(inviteCode).build();

        when(mailProperties.isOverrideClasspathTemplates()).thenReturn(true);
        when(mailProperties.getAbsoluteEmailTemplatesPath()).thenReturn("/templates");
        when(mailProperties.getRegisterFrontendUrl()).thenReturn("/sign-up");
        when(pebbleService.generateHTML(anyString(), anyMap())).thenThrow(IOException.class);

        assertThrows(ApiException.class, () -> emailService.sendInviteEmail(invite));

        verify(pebbleService).generateHTML("/templates/invite.peb", Map.of(
                "name", "wout",
                "url", "/sign-up?inviteCode=" + inviteCode
        ));
        verify(mailSender, never()).send(any(MimeMessage.class));
    }
}
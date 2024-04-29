package be.zsoft.zscore.core.service.utils;

import be.zsoft.zscore.core.ErrorCodes;
import be.zsoft.zscore.core.common.exception.ApiException;
import be.zsoft.zscore.core.config.properties.MailProperties;
import be.zsoft.zscore.core.entity.user.User;
import be.zsoft.zscore.core.entity.user.UserInvite;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class SMTPEmailService {

    private static final String CLASSPATH_BASE_PATH = "mail-templates";

    private final MailProperties mailProperties;
    private final JavaMailSender mailSender;
    private final PebbleService pebbleService;

    public void sendActivationMail(User user) {
        log.debug("Sending activation mail to '{}'", user.getEmail());

        try {
            String templatePath = getPath("activate.peb");
            String html = pebbleService.generateHTML(templatePath, Map.of(
                    "name", user.getName(),
                    "url", mailProperties.getActivationFrontendUrl() + "?code=" + user.getActivationCode()
            ));
            sendMail(user.getEmail(), user.getName(), "Activate your account", html);
        } catch (Exception e) {
            log.error("Error sending email: ", e);
            throw new ApiException(ErrorCodes.INTERNAL_ERROR, e.getMessage());
        }
    }

    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset mail to '{}'", user.getEmail());

        try {
            String templatePath = getPath("password-reset.peb");
            String html = pebbleService.generateHTML(templatePath, Map.of(
                    "name", user.getName(),
                    "url", mailProperties.getResetPasswordFrontendUrl() + "?code=" + user.getActivationCode()
            ));
            sendMail(user.getEmail(), user.getName(), "Reset your password", html);
        } catch (Exception e) {
            log.error("Error sending email: ", e);
            throw new ApiException(ErrorCodes.INTERNAL_ERROR, e.getMessage());
        }
    }

    public void sendInviteEmail(UserInvite invite) {
        log.debug("Sending invite mail to '{}'", invite.getEmail());

        try {
            String templatePath = getPath("invite.peb");
            String html = pebbleService.generateHTML(templatePath, Map.of(
                    "name", invite.getName(),
                    "url", mailProperties.getRegisterFrontendUrl() + "?inviteCode=" + invite.getInviteCode()
            ));
            sendMail(invite.getEmail(), invite.getName(), "You have been invited to Z-Score", html);
        } catch (Exception e) {
            log.error("Error sending email: ", e);
            throw new ApiException(ErrorCodes.INTERNAL_ERROR, e.getMessage());
        }
    }

    private String getPath(String filename) {
        if (mailProperties.isOverrideClasspathTemplates()) {
            return Path.of(mailProperties.getAbsoluteEmailTemplatesPath(), filename).toString();
        }

        return Path.of(CLASSPATH_BASE_PATH, filename).toString();
    }

    private void sendMail(String toEmail, String toName, String subject, String html) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(new InternetAddress(toEmail, toName, "UTF-8"));
        helper.setFrom(new InternetAddress(mailProperties.getNoReplyEmail(), mailProperties.getNoReplyName()));
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(mimeMessage);
    }
}

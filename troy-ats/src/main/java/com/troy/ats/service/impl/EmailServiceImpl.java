package com.troy.ats.service.impl;

import com.troy.ats.constants.CommonConstants;
import com.troy.ats.entity.Candidate;
import com.troy.ats.service.EmailService;
import com.troy.ats.util.CommonUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@Service("emailService")
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final SessionServiceImpl sessionService;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String mailUsername;

    /**
     *
     * @param candidate
     * @param file
     */
    @Override
    public void sendCandidateEmail(Candidate candidate, String emailType, MultipartFile file) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    true,
                    "UTF-8"
            );

            helper.setFrom(mailUsername);
            helper.setTo(candidate.getEmail());

            /*if(Objects.nonNull(sessionService.getCurrentUser()) && Objects.nonNull(sessionService.getCurrentUser().getOfficialEmail())){
                helper.setCc(sessionService.getCurrentUser().getOfficialEmail());
            }*/

            helper.setSubject(CommonUtil.getEmailSubject(emailType));

            // HTML template
            Context context = new Context();

            context.setVariable(CommonConstants.EMAIL_TEMPLATE_VARIABLE_CANDIDATE_NAME, candidate.getFullName());

            context.setVariable(CommonConstants.EMAIL_TEMPLATE_VARIABLE_DESIGNATION, candidate.getCurrentDesignation());

            context.setVariable(CommonConstants.EMAIL_TEMPLATE_VARIABLE_EXPERIENCE, candidate.getExperienceYears());

            context.setVariable(CommonConstants.EMAIL_TEMPLATE_VARIABLE_LOCATION, candidate.getLocation());

            String html = templateEngine.process(CommonConstants.EMAIL_TEMPLATE_PATH, context);

            helper.setText(html, true);

            // Attach MultipartFile directly
            if (file != null && !file.isEmpty()) {

                helper.addAttachment(file.getOriginalFilename(), file);
            }

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

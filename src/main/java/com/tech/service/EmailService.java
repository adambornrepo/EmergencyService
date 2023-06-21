package com.tech.service;

import com.tech.configuration.ApiMessages;
import com.tech.demo.PrescriptionExportResource;
import com.tech.exception.custom.EmailSendingException;
import freemarker.template.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final ApiMessages apiMessages;
    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;


    @Value("${email.px.template}")
    private String emailTemplate;

    @Value("${email.px.subject}")
    private String emailSubject;

    @Value("${email.px.background-img}")
    private String backgroundImage;

    public void sendPrescriptionEmail(PrescriptionExportResource dataSource, String recipientEmail) {
        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Template template = freemarkerConfig.getTemplate(emailTemplate);

            StringWriter writer = new StringWriter();
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("data", dataSource);
            template.process(dataModel, writer);
            String emailContent = writer.toString();

            helper.setTo(recipientEmail);
            helper.setSubject(emailSubject);
            helper.setText(emailContent, true);

            ClassPathResource backgroundImageResource = new ClassPathResource(backgroundImage);
            helper.addInline("backgroundImage", backgroundImageResource);

            mailSender.send(message);
        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Email cannot send: {}", e.getMessage());
            throw new EmailSendingException(String.format(apiMessages.getMessage("error.send.mail.px"), e.getMessage()));
        }
    }

}

package io.github.M1lY.employeetaskmanagementtool.service;

import io.github.M1lY.employeetaskmanagementtool.exceptions.EmployeeManagementException;
import io.github.M1lY.employeetaskmanagementtool.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.*;


@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationEmail notificationEmail){
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);;
            messageHelper.setFrom("no_reply@taskmanagement.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
        };
        try {
            javaMailSender.send(messagePreparator);
            log.info("Activation mail sent");
        }catch (MailException e){
            log.error("Exception when sending email");
            throw new EmployeeManagementException("Exception when sending email to: "+ notificationEmail.getRecipient(), e);
        }
    }
}

package org.spring.thymeleaf.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.thymeleaf.model.User;
import org.spring.thymeleaf.service.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public class MailSenderServiceImpl implements MailSenderService {

    private static final Logger logger = LoggerFactory.getLogger(MailSenderServiceImpl.class);

    private final static String smtpSeverHostParam = "mail.smtp.host";
    private final static String serverAddress = "harshal210491@gmail.com";

    @Autowired
    Environment env;

    @Override
    public void sendActivationMailInformation(User user, String activationUrl) {
        try {
            Properties sendProperties = System.getProperties();
            sendProperties.put(smtpSeverHostParam, env.getProperty(smtpSeverHostParam));

            Session session = Session.getInstance(sendProperties, null);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(serverAddress));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            message.setSubject("Simplewebapp.com - Activate Your Account");
            message.addHeader("Content-Type", "text/plain; charset=UTF-8");
            message.setText("Hi, " + user.getName() + ". \n" +
                            " Please activate your account by link below: \n" + activationUrl);

            Transport.send(message);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}

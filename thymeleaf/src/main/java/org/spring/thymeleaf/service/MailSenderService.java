package org.spring.thymeleaf.service;

import org.spring.thymeleaf.model.User;

public interface MailSenderService {

    public void sendActivationMailInformation(User user, String url);
}
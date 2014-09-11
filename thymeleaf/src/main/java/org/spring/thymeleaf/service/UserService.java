package org.spring.thymeleaf.service;


import org.spring.thymeleaf.model.User;


public interface UserService {
    public void addUser(User user);

    public User getUser(int id);

    public User getUser(String email);

    public User activateUser(String activationKey);

    public void changeUser(User user);

    public void removeUser(User user);
}
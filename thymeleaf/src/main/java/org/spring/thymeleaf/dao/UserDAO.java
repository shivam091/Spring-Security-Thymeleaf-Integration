package org.spring.thymeleaf.dao;

import org.spring.thymeleaf.model.User;


public interface UserDAO {
    public void addUser(User user);

    public User getUser(int id);

    public User getUser(String email);

    public User getUserToActivate(String activationKey);

    public void changeUser(User user);

    public void removeUser(int id);
}
package org.spring.thymeleaf.service.impl;


import org.spring.thymeleaf.dao.RoleDAO;
import org.spring.thymeleaf.dao.UserDAO;
import org.spring.thymeleaf.model.User;
import org.spring.thymeleaf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
//@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userDAO.getUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        return user;
    }

//    todo: Switch Activation key to GUID generator and add custom Role management
//  Because it is simple case i allowed myself to add simple Role named "USER" and
//    using as ActivationKey Email. In real case we need to generate GUID
    @Override
    public void addUser(User user) {
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setActivationKey(user.getEmail());
        user.setUserRole(roleDAO.getRole("USER"));
        userDAO.addUser(user);
    }

    @Override
    public User activateUser(String activationKey) {
        User userToActivate = userDAO.getUserToActivate(activationKey);

        if (userToActivate != null && !userToActivate.isEnabled()) {
            userToActivate.setEnabled(true);
            userToActivate.setActivationKey(null);
            userDAO.changeUser(userToActivate);
            return userToActivate;
        }
        return null;
    }

    @Override
    public User getUser(int id) {
        return userDAO.getUser(id);
    }

    @Override
    public User getUser(String email) {
        return userDAO.getUser(email);
    }

    @Override
    public void changeUser(User user) {
        userDAO.changeUser(user);
    }

    @Override
    public void removeUser(User user) {
        userDAO.removeUser(user.getId());
    }

}

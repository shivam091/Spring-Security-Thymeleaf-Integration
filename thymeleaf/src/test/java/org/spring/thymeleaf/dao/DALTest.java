package org.spring.thymeleaf.dao;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spring.thymeleaf.config.AppConfig;
import org.spring.thymeleaf.model.Role;
import org.spring.thymeleaf.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class DALTest {
//    todo: create Factory to use mock objects

    @Autowired
    UserDAO userDAO;

    @Autowired
    RoleDAO roleDAO;

    private static User user;
    private static Role role;

    public static Role getRole() {
        if (role == null){
            role = new Role();
            role.setRole("Unit_Test_Role");
        }
        return role;
    }

    public static User getUser() {
        if (user == null) {
            user = new User();
            user.setName("UnitTest_username");
            user.setEmail("UnitTest_email");
            user.setPasswordHash("HASH");
            user.setEnabled(false);
            user.setUserRole(getRole());
        }
        return user;
    }

    @BeforeClass
    public static void setup(){

    }

    @AfterClass
    public static void teardown(){

    }

    @Test
    public void testCRUD() {
//        Add Role
        roleDAO.addRole(getRole());
//        Get Role
        Role createdRole = roleDAO.getRole(getRole().getRole());
        Assert.assertEquals(getRole().getRole(), createdRole.getRole());

//        Create User
        userDAO.addUser(getUser());

//        Get User by Email
        User updatedUser = userDAO.getUser("UnitTest_email");
        Assert.assertEquals(getUser(), updatedUser);

//        Get User by Id
        updatedUser = userDAO.getUser(updatedUser.getId());
        Assert.assertEquals(getUser(), updatedUser);

//        Update User
        String newName = "UnitTest_NewName";
        updatedUser.setName(newName);
        userDAO.changeUser(updatedUser);
        updatedUser = userDAO.getUser(updatedUser.getId());

        Assert.assertEquals(getUser().getId(), updatedUser.getId());
        Assert.assertEquals(newName, updatedUser.getName());
        Assert.assertEquals(getUser().getPasswordHash(), updatedUser.getPasswordHash());
        Assert.assertEquals(getUser().getEmail(), updatedUser.getEmail());
        Assert.assertEquals(getUser().isEnabled(), updatedUser.isEnabled());
        Assert.assertEquals(getUser().getUserRole().getRole(), updatedUser.getUserRole().getRole());

//        Add Existing User
        Exception exception = null;
        try {
         userDAO.addUser(getUser());
        } catch (RuntimeException ex){
            exception = ex;
        }
        Assert.assertNotNull(exception);

//        Delete User
        userDAO.removeUser(getUser().getId());
        updatedUser = userDAO.getUser(getUser().getId());
        Assert.assertNull(updatedUser);

//        Delete Role
        roleDAO.removeRole(createdRole.getRoleId());
        createdRole = roleDAO.getRole(getRole().getRole());
        Assert.assertNull(createdRole);
    }

    @Ignore
    @Test
    public void testConnectionPoolUser() {
        for (int i = 0; i < 100; i++) {
        User user = getUser();
        user.setEmail("UnitTest_EmailId_"+String.valueOf(i));
        userDAO.addUser(user);
        }
    }

}

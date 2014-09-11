package org.spring.thymeleaf.dao;

import org.spring.thymeleaf.model.Role;

public interface RoleDAO {

    public Role getRole(String name);

    public void addRole(Role role);

    public void removeRole(int id);
}
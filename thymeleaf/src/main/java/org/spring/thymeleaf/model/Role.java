package org.spring.thymeleaf.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@NamedQuery(name = "Role.findByRole",
        query = "select r from Role r where r.role = :role"
)
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = -2069655075653364603L;

	@Id
    @GeneratedValue
    @Column(name = "role_id", unique = true, nullable = false)
    private int roleId;

    @Column(name = "role", nullable = false, length = 100)
    private String role;

//  todo: need to change FetchType to LAZY
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userRole")
    private Set<User> userRecords = new HashSet<User>();

    @Override
    public String getAuthority() {
        return getRole();
    }

    public Set<User> getUserRecords() {
        return this.userRecords;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserRecords(Set<User> userRecords) {
        this.userRecords = userRecords;
    }

}
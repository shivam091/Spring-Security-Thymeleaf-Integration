package org.spring.thymeleaf.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "activation_key")
})
@NamedQueries({
    @NamedQuery(name = "User.findByEmail",
            query = "select u from User u where u.email = :email"
    ),
    @NamedQuery(name = "User.findByActivationKey",
        query = "select u from User u where u.activationKey = :activation_key"
    )
})
public class User implements UserDetails {

	private static final long serialVersionUID = 178093749285797583L;

	@Id @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private int id;

//    todo: need to add localization for validation messages
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

//    todo: need to add localization for validation messages
//    @NotEmpty(message = "Password is required")
    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

//    todo: need to add localization for validation messages
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

//  todo: need to change FetchType to LAZY
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role userRole;

    @Column(name = "activation_key", length = 100, nullable = true)
    private String activationKey;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;

    public User(){

    }

    public User(String email, String passwordHash, String name) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
         List<Role> roleList = new ArrayList<Role>(1);
         roleList.add(getUserRole());
         return roleList;
    }

    @Override
    public String getPassword() {
        return this.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    @Override
    public String toString() {
        return " User [ id: " + id + "\n" +
               " name: " + name + "\n" +
               " email: " + email + "\n" +
               " passwd: " + passwordHash + "\n " +
               " enabled: " + enabled + "\n" +
               "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;

        User user = (User) obj;
        return ( this.id == user.id &&
                 this.name.equals(user.name) &&
                 this.email.equals(user.email) &&
                 this.passwordHash.equals(user.passwordHash) &&
                 this.enabled == user.enabled
        );

    }
}

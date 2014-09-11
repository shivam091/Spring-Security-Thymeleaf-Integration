package org.spring.thymeleaf.web.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class RegistrationForm {

    @NotEmpty(message = "Email is required")
    @Length(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @NotEmpty(message = "Name is required")
    @Length(max = 100, message = "Name must be less than 100 characters")
    private String name;

    private String password;

    private String confirmPassword;

    public boolean isPasswordsValid(){
        return !(this.password == null || this.confirmPassword == null ||
                this.password.length() < 4 || this.confirmPassword.length() < 4);
    }

    public boolean isPasswordsEquals(){
        return (this.password != null && this.confirmPassword != null && this.confirmPassword.equals(this.password));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

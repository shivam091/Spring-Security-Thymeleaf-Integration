package org.spring.thymeleaf.web.validator;

import org.spring.thymeleaf.web.model.RegistrationForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationForm userForm = (RegistrationForm) target;

//            After debugging, i started to use constructor with errorCode. Works pretty good
//
        if (!userForm.isPasswordsValid()) {
//            errors.reject( "",
//                    new DefaultMessageSourceResolvable[]{
//                            new DefaultMessageSourceResolvable(new String[]{"userForm", "userForm.password"},
//                                    "password")
//                    }, "Password cannot be at least 4 characters"
//            );
//            errors.reject( "",
//                    new DefaultMessageSourceResolvable[]{
//                            new DefaultMessageSourceResolvable(new String[]{"userForm", "userForm.confirmPassword"},
//                                    "password")
//                    }, "Password cannot be at least 4 characters"
//            );
            errors.rejectValue("password", "", "Password cannot be at least 4 characters");
            errors.rejectValue("confirmPassword", "", "Password cannot be at least 4 characters");
        }

        if (!userForm.isPasswordsEquals()) {
            errors.rejectValue("password", "", "Provided passwords not equal");
            errors.rejectValue("confirmPassword", "", "Provided passwords not equal");
        }
    }
}

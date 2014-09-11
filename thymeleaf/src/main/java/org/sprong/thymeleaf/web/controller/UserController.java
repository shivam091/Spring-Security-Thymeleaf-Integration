package org.sprong.thymeleaf.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.thymeleaf.model.User;
import org.spring.thymeleaf.service.MailSenderService;
import org.spring.thymeleaf.service.UserService;
import org.spring.thymeleaf.web.model.RegistrationForm;
import org.spring.thymeleaf.web.validator.FormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    public static final String ACTIVATION_REQUEST_PATH = "/activate";
    public static final String ACTIVATION_REQUEST_PATH_KEY = "key";

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MailSenderService mailSenderService;

    @Autowired
    private FormValidator formValidator;

    //    Solved the problem of work with @Valid and Custom Validator
    @InitBinder("userForm")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(formValidator);
    }

    @RequestMapping("/logout")
    public String logout() {
        return "redirect:login";
    }

    @RequestMapping("/")
    public String login() {
        return "redirect:login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "logout", required = false, defaultValue = "false") String logout,
            @RequestParam(value = "registered", required = false, defaultValue = "false") String registered,
            HttpServletRequest request
    ) {
//       In our simple case i decided to use standard parameters AuthenticationFailureHandler
//       but we can create our handler

        ModelAndView model = new ModelAndView("login");

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) != null) {
            logger.error(session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION).toString());
            model.addObject("error", ((AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)).getMessage());
        }
        if (registered != null && registered.equals("true")) {
            model.addObject("registered", "You`ve been successfully registered. Please activate your account.");
        }
        if (logout != null && logout.equals("true")) {
            model.addObject("logout", "You've been logged out successfully.");
        }
        return model;
    }

    @RequestMapping(value = "/userInfo")
    public ModelAndView success(@RequestParam(value = "activate", required = false) String activate) {
        User user = null;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        ModelAndView model = new ModelAndView("userInfo", "user", user);

        if (activate != null && activate.equals("true")) {
            model.addObject("activate", "Your account has been activated.");
        }

        return model;
    }

    @RequestMapping(value = "/registration")
    public ModelAndView registration() {
        return new ModelAndView("registration");
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public String createUser(@Valid @ModelAttribute(value = "userForm") RegistrationForm userForm, BindingResult result, HttpServletRequest request) {

        if (result.hasErrors()) {
//            for (ObjectError i : result.getAllErrors()) {
//                logger.error(i.toString());
//            }
            return "registration";
        }

//        todo: Sending email and addUser must be in the same transaction
        User user = new User(userForm.getEmail(), userForm.getConfirmPassword(), userForm.getName());
        userService.addUser(user);

        String activationUrl = request.getHeader("Origin") + ACTIVATION_REQUEST_PATH + "?" + ACTIVATION_REQUEST_PATH_KEY + "=" + user.getActivationKey();
        logger.info("Activation URL: " + activationUrl);

        mailSenderService.sendActivationMailInformation(user, activationUrl);

        return "redirect:/login?registered=true";
    }

    @RequestMapping(value = ACTIVATION_REQUEST_PATH, method = RequestMethod.GET)
    public String activateUser(@RequestParam(value = ACTIVATION_REQUEST_PATH_KEY) String activationKey) {

        User user = userService.activateUser(activationKey);

        if (user != null) {
            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return "redirect:/userInfo?activate=true";
        }
        return "redirect:/login";
    }

    @ModelAttribute("userForm")
    private RegistrationForm getUser() {
        return new RegistrationForm();
    }
}

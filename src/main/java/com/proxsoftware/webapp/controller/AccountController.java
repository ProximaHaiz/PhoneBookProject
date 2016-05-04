package com.proxsoftware.webapp.controller;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.repository.AccountRepository;
import com.proxsoftware.webapp.service.AccountServiceIml1;
import com.proxsoftware.webapp.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
@PropertySource("classpath:app.properties")
public class AccountController {
    private Logger log = LoggerFactory.getLogger(AccountController.class);


    private Boolean requireActivation = false;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private List<AccountRepository> repositories;

    @Qualifier(value = "myXmlRepository")
    @Autowired
    private AccountRepository accountRepository;

    /*@Autowired
    private AuthenticationManager authenticationManager;*/

    @Autowired
    private AccountServiceIml1 accountService;

    @Autowired
    private MailService mailService;

    @RequestMapping("/")
    public String redirect() {
        return "redirect:/data/get";
    }

    @RequestMapping("/login")
    public String login(AccountEntity user) {
        System.out.println("Now logged: " + SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println("repositories: " + repositories);
        System.out.println("All beans: " + Arrays.toString(context.getBeanDefinitionNames()));
        return "user/login";
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String register(AccountEntity user) {
        return "user/register";
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public String registerPost(@Valid AccountEntity user, BindingResult result) {
        if (result.hasErrors()) {
            return "user/register";
        }

        AccountEntity registeredUser = accountService.register(user);
        if (registeredUser != null) {
            mailService.sendNewRegistration(user.getEmail(), registeredUser.getToken());
            if (!requireActivation) {
                accountService.autoLogin(user.getUserName());
                System.out.println("from act");
                return "redirect:/data/get";
            }
            return "user/register-success";
        } else {
            log.error("User already exists: " + user.getUserName());
            result.rejectValue("email", "error.alreadyExists",
                    "This username or email already exists," +
                            " please try to reset password instead.");
            return "user/register";
        }
    }


    @RequestMapping("/user/activation-send")
    public ModelAndView activationSend(AccountEntity user) {
        return new ModelAndView(
                "/user/activation-send");
    }

    @RequestMapping(value = "/user/activation-send", method = RequestMethod.POST)
    public ModelAndView activationSendPost(AccountEntity user, BindingResult result) {
        AccountEntity u = accountService.resetActivation(user.getEmail());
        if (u != null) {
            mailService.sendNewActivationRequest(u.getEmail(), u.getToken());
            return new ModelAndView("/user/activation-sent");
        } else {
            result.rejectValue("email", "error.doesntExist",
                    "We could not find this email in our databse");
            return new ModelAndView("/user/activation-send");
        }
    }

    @RequestMapping("/user/activate")
    public String activate(@RequestParam("activation") String activation) {
        AccountEntity u = accountService.activate(activation);
        System.out.println(u);
        if (u != null) {
            accountService.autoLogin(u);
            return "redirect:/";
        }
        return "redirect:/error?message=Could not" +
                " activate with this activation code," +
                " please contact support";
    }

    @RequestMapping("/user/delete")
    public String delete(Long id) {
        accountService.delete(id);
        return "redirect:/user/list";
    }

    @RequestMapping("/user/autologin")
    public String autoLogin(AccountEntity user) {
        accountService.autoLogin(user.getUserName());
        return "redirect:/";
    }

    @RequestMapping("/user/edit/{id}")
    public String edit(@PathVariable("id") Long id, AccountEntity user) {
        AccountEntity acc;
        AccountEntity loggedInUser = accountService.getLoggedInUser();
        if (id == 0) {
            id = loggedInUser.getId();
        }
        if (!Objects.equals(loggedInUser.getId(), id) && !loggedInUser.isAdmin()) {
            return "user/premission-denied";
        } else if (loggedInUser.isAdmin()) {
            acc = accountRepository.findOne(id);
        } else {
            acc = loggedInUser;
        }
        user.setId(acc.getId());
        user.setUserName(acc.getUserName());
        user.setFirstName(acc.getFirstName());
        user.setMiddleName(acc.getMiddleName());
        user.setLastName(acc.getLastName());

        return "/user/edit";
    }

    public String editPost(@Valid AccountEntity user, BindingResult result) {
        if (result.hasFieldErrors("userNname")) {
            return "/user/edit";
        }
        if (accountService.getLoggedInUser().isAdmin()) {
            accountService.updateUser(user);
        } else {
            accountService.updateUser(accountService.getLoggedInUser().getUserName(), user);
        }
        if (Objects.equals(accountService.getLoggedInUser().getId(), (user.getId()))) {
            accountService.getLoggedInUser(true);
        }

        return "redirect:/user/edit" + user.getId() + "?updated";
    }

}

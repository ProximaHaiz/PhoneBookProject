package com.proxsoftware.webapp.controller;

import com.proxsoftware.webapp.entity.AccountEntity;
import com.proxsoftware.webapp.entity.ContactEntity;
import com.proxsoftware.webapp.service.IContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/data")
public class ContactController {
    private static final String CURRENT_USER = "CURRENT_USER";
    private Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private IContactService contactService;

    @Autowired
    private HttpSession httpSession;

    @RequestMapping(value = "/phone", method = RequestMethod.GET)
    public String phone() {
        return "/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String create2() {
        return "index";
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String getAllContacts(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ContactEntity> contacts = contactService.findAllByAccount((AccountEntity) httpSession.getAttribute(CURRENT_USER));
        model.addAttribute("contacts", contacts);
        return "user/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String addNewContact(Model model) {
        model.addAttribute("contactEntity", new ContactEntity());

        return "/user/new";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editContact(@PathVariable long id,
                              Model model) {
        ContactEntity one = contactService.findOne(id);
        log.info("Editing contact: " + one + "\n id = " + id);
        model.addAttribute("contact", one);
        return "/user/edit";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createContact(@Valid ContactEntity user, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Contact creating validate error:  " + result.getAllErrors());
            return "user/new";
        }
        user.setUser((AccountEntity) httpSession.getAttribute(CURRENT_USER));
        contactService.save(user);
        return "redirect:/data/get";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateContact(@Valid ContactEntity contact, BindingResult result, Model model) {
        if (result.hasErrors()) {
            log.error("Contact update validation error: " + result.getAllErrors());
            model.addAttribute("contact", contact);
            return "user/edit";
        }
        contact.setUser((AccountEntity) httpSession.getAttribute(CURRENT_USER));
        log.info("Contact in update:" + contact);
        contactService.save(contact);
        return "redirect:/data/get";
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public ModelAndView deleteContact(@PathVariable long id) {
        contactService.delete(id);
        log.info("Contact with id = " + id + " deleted");
        return new ModelAndView("redirect:/data/get");
    }
}




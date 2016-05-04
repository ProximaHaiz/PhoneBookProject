package com.proxsoftware.webapp.controller;

import com.proxsoftware.webapp.entity.ContactEntity;
import com.proxsoftware.webapp.service.IContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/data")
public class ContactController {

    @Autowired
    @Qualifier(value = "xmlContactService")
    private IContactService contactService;

    @Autowired
    private HttpSession httpSession;


    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String getAllContacts(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ContactEntity> all = contactService.findAll();
        System.out.println("All list: " + all);

        System.out.println(principal);
        model.addAttribute("contacts", all);
        return "user/list";
    }


    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String addNewContact() {
        return "/user/new";
    }


    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editContact(@PathVariable long id,
                              Model model) {
        model.addAttribute("contact", contactService.findOne(id));
        return "/user/edit";
    }

    //TODO Сделать для new и editContact 1 страницу

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView createContact(@ModelAttribute ContactEntity user) {
        System.out.println("User from controller /create:" + user);
        contactService.save(user);
        System.out.println("save:" + user);
        return new ModelAndView("redirect:/data/get");
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateContact(@ModelAttribute ContactEntity contact
            /*@RequestParam("contact_id") long id,
                                      @RequestParam("firstName") String firstName,
                                      @RequestParam("lastName") String lastName,
                                      @RequestParam("middleName") String middleName,
                                      @RequestParam("mobileNumber") int mobileNumber,
                                      @RequestParam("homeNumber") int homeNumber,
                                      @RequestParam("address") String address,
                                      @RequestParam("email") String email*/) {
      /*  ContactEntity contact = contactService.findOne(id);
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setMiddle_name(middleName);
        contact.setMobile_number(mobileNumber);
        contact.setHome_number(homeNumber);
        contact.setAddress(address);
        contact.setEmail(email);*/
        contactService.save(contact);
        //TODO не обновляется
        System.out.println("contact " + contact.getFirstName() + " updated");
        return new ModelAndView("redirect:/data/get");
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public ModelAndView deleteContact(@PathVariable long id) {
        System.out.println("id for delete: " + id);
        contactService.delete(id);
        return new ModelAndView("redirect:/data/get");
    }
}




package com.tpe.contactmessage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactPageController {

    // =========================================
    // CONTACT PAGE
    // =========================================
    // GET http://localhost:8080/contact
    //
    // Opens the public Contact Us page.
    // No login is required.
    // =========================================

    @GetMapping("/contact")
    public String showContactPage() {

        return "contact";
    }
}
package com.projet.depouilledentaire.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "index";
    }


//    @GetMapping("/profaccount")
//    public String profaccount() {
//        return "profaccount";
//    }




    @RequestMapping(value = "/notfound", method = RequestMethod.GET)
    public String notFoundPage() {
        return "notfound";
    }


    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public String redirectToNotFound() {
        return "redirect:/notfound";
    }

}

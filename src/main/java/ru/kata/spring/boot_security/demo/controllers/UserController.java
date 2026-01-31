package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;

@Controller
@RequestMapping("/user")
public class UserController {

//    @GetMapping("/user")
    @GetMapping
    public String showUser(Model model, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "user";
    }


}

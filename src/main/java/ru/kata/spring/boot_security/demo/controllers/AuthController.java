package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RegistrationService;

@RequestMapping("/auth")
@Controller
public class AuthController {
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage(){
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(@ModelAttribute("user") User user){
        return "auth/register";
    }
    @PostMapping("/register")
    public String saveRegister(@ModelAttribute("user") User user, BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return "auth/register";

        registrationService.register(user);

        return "redirect:/auth/login";
    }
}

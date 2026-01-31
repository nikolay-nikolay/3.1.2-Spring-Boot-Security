package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")

public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping
    public String showUser(Model model, Principal principal){
        model.addAttribute("currentUsername", principal.getName());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newUser", new User());
        return "admin/show";
    }



    @PostMapping
    public String createUser(@ModelAttribute("newUser") User user,
                             @RequestParam(value = "roleIds", required = false) Set<Long> roleIds,
                             BindingResult bindingResult, Model model){

        if (bindingResult.hasErrors()){
            model.addAttribute("newUser", user);
            model.addAttribute("users", userRepository.findAll());
            return "redirect:/admin/show";
        }
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "admin/show";
    }


    @PatchMapping("/{id}")
    public String updateUser(
            @ModelAttribute("newUser") User user,
                         @RequestParam(value = "roleIds", required = false) Set<Long> roleIds,
                         BindingResult bindingResult, Model model
                         ){

        if (bindingResult.hasErrors()){
            model.addAttribute("users", userRepository.findAll());
            return "redirect:/admin";
        }


        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }


}

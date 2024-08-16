package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;

@Service
public class SupportingServiceImpl implements SupportingService {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public SupportingServiceImpl(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void createModelForView(Model model, Principal principal) {
        model.addAttribute("authUser", userService.findByUsername(principal.getName()));
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
    }
}

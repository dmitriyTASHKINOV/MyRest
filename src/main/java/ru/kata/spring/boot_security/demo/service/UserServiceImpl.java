package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.exceptions.NoSuchUserException;
import ru.kata.spring.boot_security.demo.model.Role;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UsersRepository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUsers() {
        return usersRepository.findAll();
    }

    @Transactional
    @Override
    public void add(User user,Set<String> roleNames) {
        user.setRoles(roleNames.stream().map(roleService::getByName).collect(Collectors.toSet()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);

    }
    public User findById(Long id) {
        return usersRepository.findById(id).orElseThrow(() -> new NoSuchUserException("There is no employee with ID = '" + id + "' in Database"));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        findById(id);
        usersRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void update(User user) {
        User existingUser = usersRepository.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            if (user.getPassword() == null || user.getPassword().isEmpty() || user.getPassword().equals(existingUser.getPassword())) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            Set<Role> currentRoles = existingUser.getRoles();

            if (!user.getRoles().isEmpty()) {
                for (Role role : user.getRoles()) {
                    currentRoles.add(roleService.getByName(role.getName()));
                }
                user.setRoles(currentRoles);
            } else {

                user.setRoles(currentRoles);
            }

            usersRepository.save(user);
        }
    }
}

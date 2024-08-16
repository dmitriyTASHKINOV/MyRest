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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public void add(User user) {
        user.setRoles(user.getRoles().stream().map(role -> roleService.getByName(role.getName())).collect(Collectors.toSet()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Убедитесь, что у пользователя не установлен идентификатор
        if (user.getId() != null) {
            user.setId(null); // Сбросить идентификатор, чтобы база данных могла установить его автоматически
        }
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
        user.setRoles(user.getRoles().stream().map(role -> roleService.getByName(role.getName())).collect(Collectors.toSet()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
    }
}

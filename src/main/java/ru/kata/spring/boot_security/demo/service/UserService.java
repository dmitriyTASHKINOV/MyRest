package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UsersRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService  {
    List<User> listUsers();
    User findById(Long id);
    User findByUsername(String username);

    void add(User user);
    void deleteById(Long id);
    void update(User user);


}

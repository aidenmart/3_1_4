package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    void saveUser(User user);

    void delete(Long id);

    User getById(Long id);

    void updateUser(User user);

    User getAuthUser();

    List<Role> getAllRoles();
}
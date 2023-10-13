package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.DAO.RoleDAO;
import ru.kata.spring.boot_security.demo.DAO.UesrDAO;

import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    private UesrDAO userJpaRepository;
    private RoleDAO roleJpaRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserServiceImpl(UesrDAO userJpaRepository, RoleDAO roleJpaRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userJpaRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), user.getAuthorities());
    }

    @Override
    public List<User> getAllUsers() {
        return userJpaRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userJpaRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userJpaRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = userJpaRepository.findById(id).get();
        user.setRoles(null);
        userJpaRepository.save(user);
        userJpaRepository.deleteById(id);
    }

    @Override
    public User getById(Long id) {
        return userJpaRepository.getById(id);
    }

    @Override
    public User getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userJpaRepository.findByName(auth.getName());
    }

    @Override
    public List<Role> getAllRoles() {
        return roleJpaRepository.findAll();
    }
}

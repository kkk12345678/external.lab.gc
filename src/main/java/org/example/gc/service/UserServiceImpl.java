package org.example.gc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.UserDto;
import org.example.gc.entity.Role;
import org.example.gc.entity.User;
import org.example.gc.parameters.UserParameters;
import org.example.gc.repository.RoleRepository;
import org.example.gc.repository.UserRepository;
import org.example.gc.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl extends AbstractService implements UserService {
    private static final String ERROR_PARAMS_VIOLATION =
            "User parameters have the following violations : [%s]";
    private static final String ERROR_NAME_ALREADY_EXISTS =
            "User with name '%s' already exists.";
    private static final String ERROR_ROLE_DOES_NOT_EXISTS =
            "There is no role with name '%s'.";
    private static final String ERROR_ID_NOT_FOUND =
            "There is no user with 'id' = '%d'.";
    private static final String MESSAGE_USERS_FOUND =
            "%d users were successfully found.";
    private static final String MESSAGE_NO_USER_BY_ID_FOUND =
            "No user with 'id' = '%d' was found.";
    private static final String ERROR_INVALID_CREDENTIALS =
            "User's credentials are invalid.";
    private static final String ERROR_NOT_FOUND =
            "User with name '%s' was not found.";
    private static final String ERROR_INVALID_PASSWORD =
            "Invalid password.";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public List<User> getAll(UserParameters parameters) {
        List<User> users = userRepository.getAll(parameters);
        log.info(String.format(MESSAGE_USERS_FOUND, users.size()));
        return users;
    }

    @Override
    @Transactional
    public User add(UserDto userDto) {
        validate(userDto);
        String name = userDto.getName();
        if (userRepository.getByName(name) != null) {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        User user = new User();
        user.setName(name);
        user.setRole(roleRepository.getByName("user"));
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user = userRepository.insertOrUpdate(user);
        log.info(String.format(MESSAGE_INSERTED, user));
        return user;
    }

    @Override
    @Transactional
    public void remove(Long id) {
        User user = check(id);
        userRepository.delete(user);
        log.info(String.format(MESSAGE_DELETED, user));
    }

    @Override
    public User getById(Long id) {
        User user = check(id);
        log.info(String.format(MESSAGE_FOUND, user));
        return user;
    }

    @Override
    @Transactional
    public User update(Long id, UserDto userDto) {
        User user = check(id);
        String name = userDto.getName();
        validate(userDto);
        User storedUser = userRepository.getByName(name);
        if (storedUser != null && !storedUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        user.setPassword(userDto.getPassword());
        user.setName(name);
        user = userRepository.insertOrUpdate(user);
        log.info(String.format(MESSAGE_UPDATED, user));
        return user;
    }

    @Override
    public String login(UserDto dto) {
        User user = userRepository.getByName(dto.getName());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(ERROR_INVALID_CREDENTIALS);
        }
        try {
            dto.setPassword(user.getPassword());
            return jwtTokenProvider.createToken(dto.getName(), user.getRole());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public String signup(UserDto dto) {
        String name = dto.getName();
        if (userRepository.getByName(name) != null) {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        User user = new User();
        user.setName(name);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role role = roleRepository.getByName("user");
        user.setRole(role);
        userRepository.insertOrUpdate(user);
        return jwtTokenProvider.createToken(name, role);
    }

    private User check(Long id) {
        User user = userRepository.getById(id);
        if (user == null) {
            log.info(String.format(MESSAGE_NO_USER_BY_ID_FOUND, id));
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        return user;
    }
}
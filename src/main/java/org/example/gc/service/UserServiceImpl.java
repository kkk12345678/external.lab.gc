package org.example.gc.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.UserDto;
import org.example.gc.dto.UserLoginDto;
import org.example.gc.dto.UserSignupDto;
import org.example.gc.entity.User;
import org.example.gc.exception.AlreadyExistsException;
import org.example.gc.exception.NoSuchUserException;
import org.example.gc.parameters.UserParameters;
import org.example.gc.repository.RoleRepository;
import org.example.gc.repository.UserRepository;
import org.example.gc.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    public List<UserDto> getAll(UserParameters parameters) {
        List<UserDto> users = userRepository.getAll(parameters)
                .stream().map(User::toDto)
                .collect(Collectors.toList());
        log.info(String.format(MESSAGE_USERS_FOUND, users.size()));
        return users;
    }

    @Override
    @Transactional
    public UserDto add(UserSignupDto dto) {
        validate(dto);
        String name = dto.getName();
        if (userRepository.getByName(name) != null) {
            throw new AlreadyExistsException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        User user = new User(null, name, passwordEncoder.encode(dto.getPassword()), roleRepository.getByName("user"));
        user = userRepository.insertOrUpdate(user);
        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getRole());
        log.info(String.format(MESSAGE_INSERTED, userDto));
        return userDto;
    }

    @Override
    @Transactional
    public void remove(Long id) {
        User user = check(id);
        userRepository.delete(user);
        log.info(String.format(MESSAGE_DELETED, user));
    }

    @Override
    public UserDto getById(Long id) {
        UserDto userDto = check(id).toDto();
        log.info(String.format(MESSAGE_FOUND, userDto));
        return userDto;
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserSignupDto dto) {
        User user = check(id);
        String name = user.getName();
        validate(dto);
        User storedUser = userRepository.getByName(name);
        if (storedUser != null && !storedUser.getId().equals(user.getId())) {
            throw new AlreadyExistsException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(name);
        UserDto userDto = userRepository.insertOrUpdate(user).toDto();
        log.info(String.format(MESSAGE_UPDATED, userDto));
        return userDto;
    }

    @Override
    public String login(UserLoginDto dto) {
        User user = userRepository.getByName(dto.getName());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new NoSuchUserException(ERROR_INVALID_CREDENTIALS);
        }
        return jwtTokenProvider.createToken(user.toDto());
    }

    @Override
    @Transactional
    public String signup(UserSignupDto dto) {
        String name = dto.getName();
        if (userRepository.getByName(name) != null) {
            throw new AlreadyExistsException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
        User user = new User(null, name, passwordEncoder.encode(dto.getPassword()), roleRepository.getByName("user"));
        userRepository.insertOrUpdate(user);
        return jwtTokenProvider.createToken(user.toDto());
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
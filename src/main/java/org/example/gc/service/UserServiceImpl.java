package org.example.gc.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.UserDto;
import org.example.gc.entity.Role;
import org.example.gc.entity.User;
import org.example.gc.parameters.UserParameters;
import org.example.gc.repository.RoleRepository;
import org.example.gc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
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
            "%d user were successfully found.";
    private static final String MESSAGE_NO_USER_BY_ID_FOUND =
            "No user with 'id' = '%d' was found.";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

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
        if (userRepository.getByName(name) == null) {
            User user = userDto.toEntity();
            String roleName = userDto.getRole();
            Role role = roleRepository.getByName(roleName);
            if (role == null) {
                throw new IllegalArgumentException(String.format(ERROR_ROLE_DOES_NOT_EXISTS, roleName));
            }
            user.setRole(role);
            user = userRepository.insertOrUpdate(user);
            log.info(String.format(MESSAGE_INSERTED, user));
            return user;
        } else {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, name));
        }
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
    public User update(Long id, UserDto userDto) {
        //TODO
        return null;
    }

    private User check(Long id) {
        User tag = userRepository.getById(id);
        if (tag == null) {
            log.info(String.format(MESSAGE_NO_USER_BY_ID_FOUND, id));
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        return tag;
    }
}

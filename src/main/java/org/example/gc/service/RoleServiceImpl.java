package org.example.gc.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.dto.RoleDto;
import org.example.gc.entity.Role;
import org.example.gc.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleServiceImpl extends AbstractService implements RoleService {
    private static final String ERROR_PARAMS_VIOLATION =
            "Role parameters have the following violations : [%s]";
    private static final String ERROR_NAME_ALREADY_EXISTS =
            "Role with name '%s' already exists.";
    private static final String ERROR_ID_NOT_FOUND =
            "There is no Role with 'id' = '%d'.";

    private static final String MESSAGE_ROLES_FOUND =
            "%d Roles were successfully found.";
    private static final String MESSAGE_NO_ROLE_BY_ID_FOUND =
            "No Role with 'id' = '%d' was found.";

    @Autowired
    private RoleRepository RoleRepository;

    @Override
    public List<Role> getAll() {
        List<Role> roles = RoleRepository.getAll();
        log.info(String.format(MESSAGE_ROLES_FOUND, roles.size()));
        return roles;
    }

    @Override
    @Transactional
    public Role add(RoleDto roleDto) {
        validate(roleDto);
        String RoleName = roleDto.getName();
        if (RoleRepository.getByName(RoleName) == null) {
            Role role = RoleRepository.insertOrUpdate(roleDto.toEntity());
            log.info(String.format(MESSAGE_INSERTED, role));
            return role;
        } else {
            throw new IllegalArgumentException(String.format(ERROR_NAME_ALREADY_EXISTS, RoleName));
        }
    }

    @Override
    @Transactional
    public void remove(Long id) {
        Role role = check(id);
        RoleRepository.delete(role);
        log.info(String.format(MESSAGE_DELETED, role));
    }

    @Override
    public Role getById(Long id) {
        Role role = check(id);
        log.info(String.format(MESSAGE_FOUND, role));
        return role;
    }


    private Role check(Long id) {
        Role role = RoleRepository.getById(id);
        if (role == null) {
            log.info(String.format(MESSAGE_NO_ROLE_BY_ID_FOUND, id));
            throw new NoSuchElementException(String.format(ERROR_ID_NOT_FOUND, id));
        }
        return role;
    }
}

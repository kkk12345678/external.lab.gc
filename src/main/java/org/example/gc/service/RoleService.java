package org.example.gc.service;


import org.example.gc.dto.RoleDto;
import org.example.gc.entity.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();
    Role add(RoleDto roleDto);
    void remove(Long id);
    Role getById(Long id);
}

package org.example.gc.repository;

import org.example.gc.entity.Role;

import java.util.List;

public interface RoleRepository extends EntityRepository<Role> {
    List<Role> getAll();
}

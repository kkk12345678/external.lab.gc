package org.example.gc.repository;

import org.example.gc.entity.User;
import org.example.gc.parameters.UserParameters;

import java.util.List;

public interface UserRepository extends EntityRepository<User> {
    List<User> getAll(UserParameters parameters);
}

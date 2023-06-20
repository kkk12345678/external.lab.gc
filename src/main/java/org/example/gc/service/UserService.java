package org.example.gc.service;


import org.example.gc.dto.UserDto;
import org.example.gc.entity.User;
import org.example.gc.parameters.UserParameters;

import java.util.List;

public interface UserService {
    List<User> getAll(UserParameters parameters);
    User add(UserDto userDto);
    void remove(Long id);
    User getById(Long id);
    User update(Long id, UserDto userDto);
}

package org.example.gc.service;

import org.example.gc.dto.UserDto;
import org.example.gc.dto.UserLoginDto;
import org.example.gc.dto.UserSignupDto;
import org.example.gc.entity.User;
import org.example.gc.parameters.UserParameters;

import java.util.List;

public interface UserService {
    List<UserDto> getAll(UserParameters parameters);
    UserDto add(UserSignupDto dto);
    void remove(Long id);
    UserDto getById(Long id);
    UserDto update(Long id, UserSignupDto dto);
    String login(UserLoginDto dto);
    String signup(UserSignupDto dto);
}
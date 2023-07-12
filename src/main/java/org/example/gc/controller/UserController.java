package org.example.gc.controller;

import org.example.gc.dto.UserDto;
import org.example.gc.dto.UserLoginDto;
import org.example.gc.dto.UserSignupDto;
import org.example.gc.parameters.UserParameters;
import org.example.gc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final String USER_ID = "userId";
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserDto> getAllUsers(UserParameters userParameters) {
        return userService.getAll(userParameters);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #id")
    public UserDto getUserIdById(@PathVariable(USER_ID) Long id) {
        return userService.getById(id);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserDto addUser(@RequestBody(required = false) UserSignupDto dto) {
        return userService.add(dto);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or principal.id == #id")
    public UserDto updateUser(@PathVariable(USER_ID) Long id, @RequestBody(required = false) UserSignupDto dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable(USER_ID) Long id) {
        userService.remove(id);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDto dto) {
        return userService.login(dto);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody UserSignupDto dto) {
        return userService.signup(dto);
    }
}
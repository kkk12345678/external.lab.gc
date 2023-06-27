package org.example.gc.controller;

import org.example.gc.dto.UserDto;
import org.example.gc.entity.User;
import org.example.gc.parameters.UserParameters;
import org.example.gc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final String USER_ID = "userId";
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers(UserParameters userParameters) {
        return userService.getAll(userParameters);
    }

    @GetMapping("/{userId}")
    public User getUserIdById(@PathVariable(USER_ID) Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody(required = false) UserDto dto) {
        return userService.add(dto);
    }

    @PutMapping("/{userId}")
    public User updateUser(@PathVariable(USER_ID) Long id, @RequestBody(required = false) UserDto dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable(USER_ID) Long id) {
        userService.remove(id);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDto dto) {
        return userService.login(dto);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody UserDto dto) {
        return userService.signup(dto);
    }
}
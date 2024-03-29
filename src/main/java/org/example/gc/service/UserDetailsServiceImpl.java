package org.example.gc.service;

import org.example.gc.entity.User;
import org.example.gc.repository.RoleRepository;
import org.example.gc.repository.UserRepository;
import org.example.gc.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {
    private static final String ERROR_USER_NOT_FOUND = "User with name '%s' is not found.";
    @Autowired
    private transient UserRepository userRepository;
    @Autowired
    private transient RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format(ERROR_USER_NOT_FOUND, username));
        }
        return new UserDetailsImpl(user);
    }
}
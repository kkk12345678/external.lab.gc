package org.example.gc.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class UserDetailsImpl extends User {
    public UserDetailsImpl(org.example.gc.entity.User user) {
        super(user.getName(), user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase())));
    }
}
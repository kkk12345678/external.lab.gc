package org.example.gc.config;

import org.example.gc.entity.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;


@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {
    /*
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
            auth
                    .requestMatchers(HttpMethod.GET).hasRole(Role.ADMIN.toString())
                    .requestMatchers(HttpMethod.GET).hasRole(Role.USER.toString())
                    .requestMatchers(HttpMethod.DELETE).hasRole(Role.ADMIN.toString())
                    .requestMatchers(HttpMethod.PUT).hasRole(Role.ADMIN.toString())
                    .requestMatchers(HttpMethod.POST).hasRole(Role.ADMIN.toString())
                    .anyRequest().permitAll();
        });
        return http.build();
    }

     */

}

package org.example.gc.config;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.util.JwtConfigurer;
import org.example.gc.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"org.example.gc"})
@Slf4j
public class SecurityConfiguration {
    private static final String GUEST_UPDATE = "/users";
    private static final String[] USER_UPDATE = {"/users/*", "/orders/*", "/orders"};
    private static final String[] ADMIN_UPDATE =
            {"/tags/*", "/gift-certificates/*", "/orders/*", "/users/*", "/tags", "/gift-certificates", "/orders", "/users"};
    private static final String[] GUEST_READ =
            {"/tags/*", "/gift-certificates/*", "/tags/*", "/gift-certificates/*"};
    private static final String[] USER_READ =
            {"/tags/*", "/gift-certificates/*", "/tags/*", "/gift-certificates/*", "/users/*"};
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .logout(l -> l.logoutUrl("/users/logout")
                        .addLogoutHandler(((request, response, authentication) -> {
                            try {
                                request.logout();
                            } catch (ServletException e) {
                                log.error(e.getMessage());
                            }
                        })))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/users/login", "users/signup").permitAll()
                        //.requestMatchers(HttpMethod.GET, ADMIN_UPDATE).hasAuthority("admin")
                        .anyRequest().authenticated())
                .apply(new JwtConfigurer(jwtTokenProvider));
        return http.build();
    }
}

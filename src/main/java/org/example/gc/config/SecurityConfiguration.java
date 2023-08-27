package org.example.gc.config;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.security.JwtConfigurer;
import org.example.gc.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.Serializable;
import java.util.List;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"org.example.gc"})
@Slf4j
public class SecurityConfiguration implements Serializable {
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
    public DaoAuthenticationProvider authProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http
                .cors(Customizer.withDefaults())
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
                        .requestMatchers(HttpMethod.GET, "/tags/**", "/gift-certificates/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login", "/users/signup", "/users/logout")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .apply(new JwtConfigurer(jwtTokenProvider));
        return http.build();
        //return http.cors(AbstractHttpConfigurer::disable).build();
    }
}
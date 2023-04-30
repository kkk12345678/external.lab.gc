package org.example.gs.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan("org.example.gs")
public class AppConfig implements WebMvcConfigurer {
    @Bean
    public JdbcTemplate jdbcTemplateObject() {
        return new JdbcTemplate(JdbcConfig.getDataSource());
    }

    @Bean
    public Logger logger(InjectionPoint injectionPoint) {
        Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
        return LogManager.getLogger(classOnWired);
    }
}

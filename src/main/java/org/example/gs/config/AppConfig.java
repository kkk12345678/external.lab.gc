package org.example.gs.config;

import org.example.gs.dao.EntityDao;
import org.example.gs.dao.JdbcTagDao;
import org.example.gs.model.Tag;
import org.example.gs.service.TagService;
import org.example.gs.service.TagServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.example.gs.controller")
public class AppConfig {
    /*
    @Bean
    public TagService tagService() {
        return new TagServiceImpl(jdbcTagDao());
    }

    @Bean
    public EntityDao<Tag> jdbcTagDao() {
        return new JdbcTagDao(JdbcConfig.getDataSource());
    }

     */
}

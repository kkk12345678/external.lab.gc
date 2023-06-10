package org.example.gc.JDBC;

import jakarta.persistence.EntityManager;
import org.example.gc.config.AppConfig;
import org.example.gc.entity.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class JpaTagDaoTest {
    /*
    @Autowired
    LocalContainerEntityManagerFactoryBean entityManagerFactory;

    @Test
    void testGetAllTags() {
        EntityManager entityManager = entityManagerFactory.createNativeEntityManager(null);
        entityManager.getTransaction().begin();
        Tag tag = new Tag(0L, "some very new tag");
        entityManager.persist(tag);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

     */
}

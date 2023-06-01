package org.example.gc.repository;

import jakarta.persistence.*;
import org.apache.logging.log4j.Logger;
import org.example.gc.JDBC.ParametersHandler;
import org.example.gc.entity.Parameters;
import org.example.gc.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final String SQL_INSERT = "insert into tags (tag_id, tag_name) values (default, :name)";
    private static final String SQL_DELETE = "delete from tags where tag_id = :id";
    private static final String SQL_SELECT = "from Tag";
    private static final String SQL_SELECT_BY_NAME = "select t from tags  t where t.tag_name = :name";

    private static final String MESSAGE_TAG_FOUND = "Tag %s was successfully found.";
    private static final String MESSAGE_TAGS_FOUND = "%d tags were successfully found.";
    private static final String MESSAGE_NO_TAG_BY_ID_FOUND = "No tag with 'id' = '%d' was found.";
    private static final String MESSAGE_NO_NAME_BY_ID_FOUND = "No tag with 'name' = '%s' was found.";
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;
    @Autowired
    Logger logger;

    @Override
    public List<Tag> getAll(Parameters parameters) {
        TypedQuery<Tag> query = entityManager.createQuery(SQL_SELECT, Tag.class);
        List<Tag> tags = query.getResultList();
        logger.info(String.format(MESSAGE_TAGS_FOUND,tags.size()));
        return tags;
    }


    @Override
    public Tag insert(Tag tag) {

        String name = tag.getName();
        //EntityManager entityManager = entityManagerFactory.createNativeEntityManager(null);
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(SQL_INSERT)
                .setParameter("name", name)
                .executeUpdate();
        entityManager.getTransaction().commit();

        //entityManager.persist(tag);

        return getByName(tag.getName());
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = entityManagerFactory.createNativeEntityManager(null);
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery(SQL_DELETE)
                .setParameter("id", id)
                .executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Override
    public Tag getById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag == null) {
            logger.info(String.format(MESSAGE_NO_TAG_BY_ID_FOUND, id));
        } else {
            logger.info(String.format(MESSAGE_TAG_FOUND, tag));
        }
        return tag;
    }

    @Override
    public Tag getByName(String name) {
        TypedQuery<Tag> tagTypedQuery =
                entityManager.createQuery(SQL_SELECT_BY_NAME, Tag.class);
        try {
            Tag tag = tagTypedQuery.setParameter("name", name).getSingleResult();
            logger.info(String.format(MESSAGE_TAG_FOUND, tag));
            return tag;
        } catch (NoResultException e) {
            logger.info(String.format(MESSAGE_NO_NAME_BY_ID_FOUND, name));
            return null;
        }
    }
}

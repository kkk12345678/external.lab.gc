package org.example.gc.repository;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.example.gc.entity.Parameters;
import org.example.gc.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Slf4j
public class TagRepositoryImpl implements TagRepository {
    private static final String SQL_INSERT = "insert into tags (tag_id, tag_name) values (default, :name)";
    private static final String SQL_DELETE = "delete from tags where tag_id = :id";
    private static final String SQL_SELECT_BY_NAME = "select t from Tag t where t.name = :name";
    private static final String MESSAGE_NO_NAME_BY_ID_FOUND = "No tag with 'name' = '%s' was found.";
    private static final String MESSAGE_TAG_FOUND = "Tag %s was successfully found.";

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Tag> getAll(Parameters parameters) {
        return null;
    }

    @Override
    public Tag insert(Tag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        entityManager.clear();
        return tag;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Tag getById(Long id) {
        return null;
    }

    @Override
    public Tag getByName(String name) {
        TypedQuery<Tag> tagTypedQuery =
                entityManager.createQuery(SQL_SELECT_BY_NAME, Tag.class);
        try {
            Tag tag = tagTypedQuery.setParameter(FIELD_NAME, name).getSingleResult();
            log.info(String.format(MESSAGE_TAG_FOUND, tag));
            return tag;
        } catch (NoResultException e) {
            log.info(String.format(MESSAGE_NO_NAME_BY_ID_FOUND, name));
            return null;
        }
    }
}

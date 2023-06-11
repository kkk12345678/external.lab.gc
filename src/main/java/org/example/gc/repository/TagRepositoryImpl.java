package org.example.gc.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.example.gc.entity.Parameters;
import org.example.gc.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class TagRepositoryImpl implements TagRepository {
    private static final String JPQL_BY_NAME = "select t from Tag t where t.name = :name";
    private static final String JPQL_ALL = "from Tag";
    private static final String SEARCH_PATTERN = "%%%s%%";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> getAll(Parameters parameters) {
        return entityManager.createQuery(JPQL_ALL, Tag.class).getResultList();
    }

    @Override
    public Tag insertOrUpdate(Tag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return tag;
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }

    @Override
    public Tag getById(Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag getByName(String name) {
        try {
            return entityManager.createQuery(JPQL_BY_NAME, Tag.class)
                    .setParameter(FIELD_NAME, name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Tag> searchByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root).where(criteriaBuilder.like(root.get(FIELD_NAME), String.format(SEARCH_PATTERN, name)));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
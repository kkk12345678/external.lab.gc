package org.example.gc.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.gc.entity.Tag;
import org.example.gc.parameters.TagParameters;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Repository
public class TagRepositoryImpl implements TagRepository, Serializable {
    private static final String JPQL_BY_NAME = "select t from Tag t where t.name = :name";
    private static final String JPQL_ALL = "from Tag";
    private static final String SQL_MOST_VALUABLE = """
            select * from tags
            where tag_id in (
            select tag_id from gift_certificate_tags
            where gift_certificate_id in (
            select gift_certificate_id from orders
            where user_id = (select user_id from orders order by sum limit 1)))
            group by tag_id order by count(tag_id) desc limit 1;
            """;
    @PersistenceContext
    private transient EntityManager entityManager;

    @Override
    public List<Tag> getAll(TagParameters parameters) {
        TypedQuery<Tag> query = entityManager.createQuery(JPQL_ALL, Tag.class);
        setPagination(query, parameters);
        return query.getResultList();
    }

    @Override
    public Tag getMostValuable() {
        try {
            Query query = entityManager.createNativeQuery(SQL_MOST_VALUABLE, Tag.class);
            return (Tag) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Tag insertOrUpdate(Tag tag) {
        entityManager.persist(tag);
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
    public List<Tag> getByNames(Set<String> tagNames) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        In<String> inClause = criteriaBuilder.in(root.get(FIELD_NAME));
        tagNames.forEach(inClause::value);
        criteriaQuery.where(inClause);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
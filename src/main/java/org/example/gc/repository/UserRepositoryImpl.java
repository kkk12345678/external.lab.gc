package org.example.gc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.example.gc.entity.User;
import org.example.gc.parameters.UserParameters;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String JPQL_BY_NAME = "select t from User t where t.name = :name";
    private static final String JPQL_ALL = "from User";

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public User insertOrUpdate(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public User getById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getByName(String name) {
        try {
            return entityManager.createQuery(JPQL_BY_NAME, User.class)
                    .setParameter(FIELD_NAME, name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<User> getAll(UserParameters parameters) {
        TypedQuery<User> query = entityManager.createQuery(JPQL_ALL, User.class);
        setPagination(query, parameters);
        return query.getResultList();
    }
}

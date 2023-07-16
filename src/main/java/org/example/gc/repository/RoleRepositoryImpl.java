package org.example.gc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.example.gc.entity.Role;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository, Serializable {
    private static final String JPQL_BY_NAME = "select t from Role t where t.name = :name";
    private static final String JPQL_ALL = "from Role";
    @PersistenceContext
    private transient EntityManager entityManager;

    @Override
    public Role insertOrUpdate(Role role) {
        entityManager.persist(role);
        return role;
    }

    @Override
    public void delete(Role role) {
        entityManager.remove(role);
    }

    @Override
    public Role getById(Long id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Role getByName(String name) {
        try {
            return entityManager.createQuery(JPQL_BY_NAME, Role.class)
                    .setParameter(FIELD_NAME, name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Role> getAll() {
        return entityManager.createQuery(JPQL_ALL, Role.class).getResultList();
    }
}

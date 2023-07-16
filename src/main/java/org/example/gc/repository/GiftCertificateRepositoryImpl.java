package org.example.gc.repository;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.Order;
import org.example.gc.entity.*;
import org.example.gc.parameters.GiftCertificateParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {
    private static final String JPQL_SELECT_BY_NAME = "select gc from GiftCertificate gc where gc.name = :name";
    private static final String SEARCH_PATTERN = "%%%s%%";
    private static final String SPLIT_REGEX = ",";

    private static final String FIELD_TAGS = "tags";
    private static final String FIELD_DATE = "createDate";
    private static final String PARAM_DATE = "date";

    private static final String SORT_ASC = "asc";
    private static final String SORT_DESC = "desc";


    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<GiftCertificate> getAll(GiftCertificateParameters parameters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery =
                criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        setSearch(parameters, criteriaBuilder, criteriaQuery, root);
        setSort(parameters, criteriaBuilder, criteriaQuery, root);
        criteriaQuery.select(root);
        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);
        setPagination(query, parameters);
        return query.getResultList();
    }

    @Override
    public GiftCertificate insertOrUpdate(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        return giftCertificate;
    }

    @Override
    public void delete(GiftCertificate giftCertificate) {
        entityManager.remove(giftCertificate);
    }

    @Override
    public GiftCertificate getById(Long id) {
        return entityManager.find(GiftCertificate.class, id);
    }

    @Override
    public GiftCertificate getByName(String name) {
        try {
            return entityManager.createQuery(JPQL_SELECT_BY_NAME, GiftCertificate.class)
                    .setParameter(FIELD_NAME, name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private List<Long> searchByTagName(String tagName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery =
                criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        root.join(GiftCertificate_.tags);
        criteriaQuery.select(root).distinct(true)
                .where(criteriaBuilder.like(root.get(FIELD_TAGS).get(FIELD_NAME),
                        String.format(SEARCH_PATTERN, tagName)));
        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList().stream().map(GiftCertificate::getId).toList();
    }

    private void setSearch(GiftCertificateParameters giftCertificateParameters,
                           CriteriaBuilder criteriaBuilder,
                           CriteriaQuery<GiftCertificate> criteriaQuery,
                           Root<GiftCertificate> root) {

        String[] search = giftCertificateParameters.getSearch();
        List<Predicate> predicates = new ArrayList<>();
        if (search != null && search.length > 0) {
            Arrays.stream(search).forEach(s -> {
                String[] parts = s.split(SPLIT_REGEX);
                predicates.add(criteriaBuilder.like(root.get(parts[0]), String.format(SEARCH_PATTERN, parts[1])));
            });
        }
        String[] tagNames = giftCertificateParameters.getTagNames();
        if (tagNames != null) {
            Arrays.stream(tagNames).forEach(tagName -> {
                In<Long> inClause = criteriaBuilder.in(root.get(FIELD_ID));
                searchByTagName(tagName).forEach(inClause::value);
                predicates.add(inClause);
            });
        }
        Predicate[] array = new Predicate[predicates.size()];
        predicates.toArray(array);
        criteriaQuery.where(array);
    }

    private void setSort(GiftCertificateParameters giftCertificateParameters,
                         CriteriaBuilder criteriaBuilder,
                         CriteriaQuery<GiftCertificate> criteriaQuery,
                         Root<GiftCertificate> root) {
        String[] sort = giftCertificateParameters.getSort();
        if (sort != null && sort.length > 0) {
            List<Order> orders = new ArrayList<>();
            Arrays.stream(sort).forEach(s -> setOrders(s, orders, criteriaBuilder, root));
            criteriaQuery.orderBy(orders);
        }
    }

    private static void setOrders(String s, List<Order> orders,
                                  CriteriaBuilder criteriaBuilder,
                                  Root<GiftCertificate> root) {
        String[] parts = s.split(SPLIT_REGEX);
        if (parts[0].equals(FIELD_NAME) && parts[1].equals(SORT_ASC)) {
            orders.add(criteriaBuilder.asc(root.get(FIELD_NAME)));
        }
        if (parts[0].equals(FIELD_NAME) && parts[1].equals(SORT_DESC)) {
            orders.add(criteriaBuilder.desc(root.get(FIELD_NAME)));
        }
        if (parts[0].equals(PARAM_DATE) && parts[1].equals(SORT_ASC)) {
            orders.add(criteriaBuilder.asc(root.get(FIELD_DATE)));
        }
        if (parts[0].equals(PARAM_DATE) && parts[1].equals(SORT_DESC)) {
            orders.add(criteriaBuilder.desc(root.get(FIELD_DATE)));
        }
    }
}
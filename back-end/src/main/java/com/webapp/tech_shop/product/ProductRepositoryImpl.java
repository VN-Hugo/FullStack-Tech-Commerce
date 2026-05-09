package com.webapp.tech_shop.product;

import com.webapp.tech_shop.product.dto.ProductSearchCriteria;
import com.webapp.tech_shop.product.model.Brand;
import com.webapp.tech_shop.product.model.Category;
import com.webapp.tech_shop.product.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> search(ProductSearchCriteria criteria, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        root.fetch("brand", JoinType.LEFT);
        root.fetch("categories", JoinType.LEFT);

        List<Predicate> predicates = buildPredicates(criteria, cb, root);
        query.select(root).distinct(true);
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        if (pageable.getSort().isSorted()) {
            query.orderBy(pageable.getSort().stream()
                    .map(order -> order.isAscending()
                            ? cb.asc(root.get(order.getProperty()))
                            : cb.desc(root.get(order.getProperty())))
                    .toList());
        }

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Product> content = typedQuery.getResultList();
        long total = count(criteria);
        return new PageImpl<>(content, pageable, total);
    }

    private long count(ProductSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Product> root = countQuery.from(Product.class);

        List<Predicate> predicates = buildPredicates(criteria, cb, root);
        countQuery.select(cb.countDistinct(root));
        if (!predicates.isEmpty()) {
            countQuery.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    private List<Predicate> buildPredicates(ProductSearchCriteria criteria, CriteriaBuilder cb, Root<Product> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(criteria.name())) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + criteria.name().toLowerCase() + "%"));
        }

        if (criteria.priceMin() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("price"), criteria.priceMin()));
        }

        if (criteria.priceMax() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("price"), criteria.priceMax()));
        }

        if (criteria.brandId() != null || StringUtils.hasText(criteria.brandName())) {
            Join<Product, Brand> brandJoin = root.join("brand", JoinType.INNER);
            if (criteria.brandId() != null) {
                predicates.add(cb.equal(brandJoin.get("id"), criteria.brandId()));
            }
            if (StringUtils.hasText(criteria.brandName())) {
                predicates.add(cb.like(cb.lower(brandJoin.get("name")), "%" + criteria.brandName().toLowerCase() + "%"));
            }
        }

        if (criteria.categoryId() != null || StringUtils.hasText(criteria.categoryName())) {
            Join<Product, Category> categoryJoin = root.join("categories", JoinType.INNER);
            if (criteria.categoryId() != null) {
                predicates.add(cb.equal(categoryJoin.get("id"), criteria.categoryId()));
            }
            if (StringUtils.hasText(criteria.categoryName())) {
                predicates.add(cb.like(cb.lower(categoryJoin.get("name")), "%" + criteria.categoryName().toLowerCase() + "%"));
            }
        }

        return predicates;
    }
}

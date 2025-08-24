package com.example.project.specs;

import com.example.project.entities.Devotee;
import com.example.project.entities.Expense;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SpecificationsQueryBuilder {
    public static Specification<Devotee> specificationForDevotee(String firstName, String lastName, String city,
                                                                 String state, Integer donationType) {
        return (root, query, cb) -> {
            if (query != null && !Long.class.equals(query.getResultType())) {
                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(firstName)) {
                predicates.add(cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%"));
            }
            if (StringUtils.isNotBlank(lastName)) {
                predicates.add(cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%"));
            }
            if (StringUtils.isNotBlank(city)) {
                Join<Object, Object> addressJoin = root.join("address", JoinType.LEFT);
                predicates.add(
                        cb.like(cb.lower(addressJoin.get("city")),
                                "%" + city.toLowerCase() + "%")
                );
            }
            if (StringUtils.isNotBlank(state)) {
                Join<Object, Object> addressJoin = root.join("address", JoinType.LEFT);
                predicates.add(
                        cb.like(cb.lower(addressJoin.get("state")),
                                "%" + state.toLowerCase() + "%")
                );
            }
            if (donationType != null) {
                Join<Object, Object> donationsJoin = root.join("donations", JoinType.INNER);
                predicates.add(
                        cb.equal(donationsJoin.get("donationType"), donationType)
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Expense> specificationsForExpense(String name, BigDecimal amount, Integer expenseType) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.isNotBlank(name)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (amount != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), amount)
                );
            }
            if (expenseType != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("expenseType"), expenseType)
                );
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

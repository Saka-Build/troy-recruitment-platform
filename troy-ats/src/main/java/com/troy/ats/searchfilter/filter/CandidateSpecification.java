package com.troy.ats.searchfilter.filter;


import com.troy.ats.entity.Candidate;
import com.troy.ats.enums.CandidateStatus;
import com.troy.ats.searchfilter.dto.CandidateFilter;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CandidateSpecification {

    private CandidateSpecification() {
    }

    public static Specification<Candidate> filter(CandidateFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Search name / email
            if (filter.search() != null && !filter.search().isBlank()) {

                String search = "%" + filter.search().toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("fullName")), search);
                Predicate cvIdPredicate = cb.like(cb.lower(root.get("cvId")), search);
                Predicate cvOwnerPredicate = cb.like(cb.lower(root.get("cvOwner").get("fullName")), search);
                Predicate emailPredicate = cb.like(cb.lower(root.get("email")), search);
                Predicate designationPredicate = cb.like(cb.lower(root.get("currentDesignation")), search);
                /*
                 * PostgreSQL skills[] search
                 *
                 * Converts the array to text and searches inside it.
                 */
                Expression<String> skillsExpression = cb.function("array_to_string", String.class, root.get("skills"), cb.literal(","));
                Predicate skillPredicate = cb.like(cb.lower(skillsExpression), search);

                predicates.add(cb.or(namePredicate, emailPredicate,designationPredicate, skillPredicate));
            }

            // Status
            if (filter.status() != null) {

                predicates.add(cb.equal(root.get("status"), CandidateStatus.fromValue(filter.status())));
            }

            // Created from
            if (filter.createdFrom() != null) {

                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.createdFrom()));
            }

            // Created to
            if (filter.createdTo() != null) {

                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.createdTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

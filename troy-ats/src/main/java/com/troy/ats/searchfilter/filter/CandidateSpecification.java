package com.troy.ats.searchfilter.filter;


import com.troy.ats.entity.Candidate;
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

            //Active
            if (filter.active() != null) {
                predicates.add(cb.equal(root.get("active"), filter.active()));
            }

            // Status
            if (filter.statusId() != null) {

                predicates.add(cb.equal(root.get("status").get("id"), filter.statusId()));
            }

            // Sub Status
            if (filter.subStatusId() != null) {

                predicates.add(cb.equal(root.get("subStatus").get("id"), filter.subStatusId()));
            }

            // Job
            if (filter.jobId() != null) {

                predicates.add(cb.equal(root.get("job").get("id"), filter.jobId()));
            }

            // Location
            if (filter.location() != null && !filter.location().isBlank()) {

                predicates.add(cb.like(cb.lower(root.get("location")), "%" + filter.location().toLowerCase() + "%"));
            }

            // Source
            if (filter.source() != null && !filter.source().isBlank()) {

                predicates.add(cb.equal(cb.lower(root.get("source")), filter.source().toLowerCase()));
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

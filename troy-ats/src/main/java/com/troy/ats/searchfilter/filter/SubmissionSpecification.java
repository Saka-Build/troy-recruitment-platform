package com.troy.ats.searchfilter.filter;

import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.searchfilter.dto.SubmissionFilter;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SubmissionSpecification {

    private SubmissionSpecification() {
    }

    public static Specification<Submission> filter(SubmissionFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Search name / email
            if (filter.search() != null && !filter.search().isBlank()) {

                String search = "%" + filter.search().toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("candidate").get("fullName")), search);
                Predicate designationPredicate = cb.like(cb.lower(root.get("candidate").get("currentDesignation")), search);
                Predicate cvIdPredicate = cb.like(cb.lower(root.get("candidate").get("cvId")), search);
                Predicate jobNamePredicate = cb.like(cb.lower(root.get("job").get("title")), search);
                Expression<String> skills = cb.function("array_to_string", String.class, root.get("job").get("skillsRequired"), cb.literal(","));
                Predicate skillsPredicate = cb.like(cb.lower(skills), "%" + search.toLowerCase() + "%");

                predicates.add(cb.or(namePredicate, designationPredicate, cvIdPredicate,jobNamePredicate,skillsPredicate));
            }

            //pipelineStage
            if (filter.pipelineStage() != null) {
                predicates.add(cb.equal(root.get("pipelineStage"), PipelineStage.fromValue(filter.pipelineStage())));
            }
            // candidate
            if (filter.candidateId() != null) {
                predicates.add(cb.equal(root.get("candidate").get("id"), filter.candidateId()));
            }
            // job
            if (filter.jobId() != null) {
                predicates.add(cb.equal(root.get("job").get("id"), filter.jobId()));
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

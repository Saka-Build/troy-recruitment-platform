package com.troy.ats.searchfilter.filter;

import com.troy.ats.entity.Submission;
import com.troy.ats.enums.PipelineStage;
import com.troy.ats.searchfilter.dto.SubmissionExportFilter;
import com.troy.ats.searchfilter.dto.SubmissionFilter;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.collections4.CollectionUtils;
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
                Predicate statusNamePredicate = cb.like(cb.lower(root.get("status").get("name")), search);
                Predicate subStatusnamePredicate = cb.like(cb.lower(root.get("subStatus").get("name")), search);
                Predicate jobNamePredicate = cb.like(cb.lower(root.get("job").get("title")), search);
                Expression<String> skills = cb.function("array_to_string", String.class, root.get("job").get("skillsRequired"), cb.literal(","));
                Predicate skillsPredicate = cb.like(cb.lower(skills), "%" + search.toLowerCase() + "%");

                predicates.add(cb.or(namePredicate, designationPredicate, cvIdPredicate, statusNamePredicate, subStatusnamePredicate, jobNamePredicate,skillsPredicate));
            }

            //pipelineStage
            if (filter.pipelineStage() != null) {
                predicates.add(cb.equal(root.get("pipelineStage"), PipelineStage.fromValue(filter.pipelineStage())));
            }
            //status id
            if (filter.statusId() != null) {
                predicates.add(cb.equal(root.get("status").get("id"), filter.statusIds()));
            }
            //Multi status ids
            if (CollectionUtils.isNotEmpty(filter.statusIds())) {
                predicates.add(root.get("status").get("id").in(filter.statusIds()));
            }

            //status name
            if (filter.statusName() != null) {
                predicates.add(cb.like(root.get("status").get("name"), filter.statusName()));
            }
            //sub status name
            if (filter.subStatusName() != null) {
                predicates.add(cb.like(root.get("subStatus").get("name"), filter.subStatusName()));
            }
            // candidate
            if (filter.candidateId() != null) {
                predicates.add(cb.equal(root.get("candidate").get("id"), filter.candidateId()));
            }
            // job
            if (filter.jobId() != null) {
                predicates.add(cb.equal(root.get("job").get("id"), filter.jobId()));
            }
            // client
            if (filter.clientId() != null) {
                predicates.add(cb.equal(root.get("job").get("client").get("id"), filter.clientId()));
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

    public static Specification<Submission> exportFilter(SubmissionExportFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // status id
            if (filter.getStatusId() != null) {
                predicates.add(cb.equal(root.get("status").get("id"), filter.getStatusId()));
            }
            //Multi status ids
            if (CollectionUtils.isNotEmpty(filter.getStatusIds())) {
                predicates.add(root.get("status").get("id").in(filter.getStatusIds()));
            }

            // job
            if (filter.getJobId() != null) {
                predicates.add(cb.equal(root.get("job").get("id"), filter.getJobId()));
            }
            // client
            if (filter.getClientId() != null) {
                predicates.add(cb.equal(root.get("job").get("client").get("id"), filter.getClientId()));
            }

            // From date
            if (filter.getCreatedFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getCreatedFrom()));
            }
            // To date
            if (filter.getCreatedTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getCreatedTo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

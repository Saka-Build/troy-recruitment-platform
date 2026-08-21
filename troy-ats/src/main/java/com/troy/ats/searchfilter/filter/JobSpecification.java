package com.troy.ats.searchfilter.filter;


import com.troy.ats.entity.Job;
import com.troy.ats.enums.JobStatus;
import com.troy.ats.searchfilter.dto.JobExportFilter;
import com.troy.ats.searchfilter.dto.JobFilter;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JobSpecification {

    private JobSpecification() {
    }

    public static Specification<Job> filter(JobFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Search name / email
            if (filter.search() != null && !filter.search().isBlank()) {

                String search = "%" + filter.search().toLowerCase() + "%";
                Predicate titlePredicate = cb.like(cb.lower(root.get("title")), search);
                Predicate jobIdPredicate = cb.like(cb.lower(root.get("jobId")), search);
                Predicate clientPersonPredicate = cb.like(cb.lower(root.get("client").get("name")), search);

                Expression<String> skills = cb.function("array_to_string", String.class, root.get("skillsRequired"), cb.literal(","));
                Predicate skillsRequiredPredicate = cb.like(cb.lower(skills), "%" + search.toLowerCase() + "%");

                Predicate locationPredicate = cb.like(cb.lower(root.get("location")), search);
                Predicate countryCodePredicate = cb.like(cb.lower(root.get("country").get("code")), search);
                Predicate countryNamePredicate = cb.like(cb.lower(root.get("country").get("name")), search);

                predicates.add(cb.or(titlePredicate, jobIdPredicate, clientPersonPredicate, skillsRequiredPredicate,locationPredicate,countryCodePredicate, countryNamePredicate));
            }

            //Country
            if (filter.countryCode() != null) {
                predicates.add(cb.equal(root.get("country").get("code"), filter.countryCode()));
            }
            //status
            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), JobStatus.fromValue(filter.status())));
            }
            //priority
            if (filter.priority() != null) {
                predicates.add(cb.equal(cb.lower(root.get("priority")), filter.priority().toLowerCase(Locale.ROOT)));
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

    public static Specification<Job> exportFilter(JobExportFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();
            // From date
            if (filter.getFromDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), filter.getFromDate()));
            }
            // To date
            if (filter.getToDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), filter.getToDate()));
            }
            // country
            if (filter.getCountryCode() != null) {
                predicates.add(cb.equal(root.get("country").get("code"), filter.getCountryCode()));
            }
            // Status
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), JobStatus.fromValue(filter.getStatus())));
            }

            query.orderBy(cb.desc(root.get("createdAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

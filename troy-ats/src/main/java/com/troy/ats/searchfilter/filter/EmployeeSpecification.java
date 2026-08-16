package com.troy.ats.searchfilter.filter;


import com.troy.ats.entity.Employee;
import com.troy.ats.searchfilter.dto.EmployeeExportFilter;
import com.troy.ats.searchfilter.dto.EmployeeFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> filter(EmployeeFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Search name / email
            if (filter.search() != null && !filter.search().isBlank()) {

                String search = "%" + filter.search().toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("fullName")), search);
                Predicate employeeCodePredicate = cb.like(cb.lower(root.get("employeeCode")), search);
                Predicate officialEmailPredicate = cb.like(cb.lower(root.get("officialEmail")), search);
                Predicate personalEmailPredicate = cb.like(cb.lower(root.get("officialEmail")), search);
                Predicate designationPredicate = cb.like(cb.lower(root.get("designation")), search);
                Predicate phonePredicate = cb.like(cb.lower(root.get("phone")), search);
                Predicate whatsappPredicate = cb.like(cb.lower(root.get("whatsapp")), search);

                predicates.add(cb.or(namePredicate, employeeCodePredicate, officialEmailPredicate,personalEmailPredicate,designationPredicate, phonePredicate,whatsappPredicate));
            }

            //Active
            if (filter.active() != null) {
                predicates.add(cb.equal(root.get("isActive"), filter.active()));
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

    public static Specification<Employee> exportFilter(EmployeeExportFilter filter) {

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
            // Role
            if (filter.getRole() != null) {
                predicates.add(cb.equal(root.get("role"), filter.getRole()));
            }
            // Designation
            if (filter.getDesignation() != null) {
                predicates.add(cb.equal(root.get("designation"), filter.getDesignation()));
            }
            // Active
            // IMPORTANT: use != null, NOT if (filter.getActive())
            if (filter.getActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), filter.getActive()));
            }

            query.orderBy(cb.desc(root.get("createdAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

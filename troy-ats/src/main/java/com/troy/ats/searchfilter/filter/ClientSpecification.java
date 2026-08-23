package com.troy.ats.searchfilter.filter;


import com.troy.ats.entity.Client;
import com.troy.ats.entity.Employee;
import com.troy.ats.searchfilter.dto.ClientExportFilter;
import com.troy.ats.searchfilter.dto.ClientFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClientSpecification {

    private ClientSpecification() {
    }

    public static Specification<Client> filter(ClientFilter filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Search name / email
            if (filter.search() != null && !filter.search().isBlank()) {

                String search = "%" + filter.search().toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), search);
                Predicate contactPersonPredicate = cb.like(cb.lower(root.get("contactPerson")), search);
                Predicate emailPredicate = cb.like(cb.lower(root.get("email")), search);
                Predicate addressPredicate = cb.like(cb.lower(root.get("address")), search);
                Predicate phonePredicate = cb.like(cb.lower(root.get("phone")), search);
                Predicate countryCodePredicate = cb.like(cb.lower(root.get("country").get("code")), search);
                Predicate countryNamePredicate = cb.like(cb.lower(root.get("country").get("name")), search);

                predicates.add(cb.or(namePredicate, contactPersonPredicate, emailPredicate,addressPredicate,phonePredicate, countryCodePredicate,countryNamePredicate));
            }

            //Active
            if (filter.active() != null) {
                predicates.add(cb.equal(root.get("isActive"), filter.active()));
            }
            //Active
            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status().toLowerCase(Locale.ROOT)));
            }

            // country
            if (filter.countryCode() != null) {
                predicates.add(cb.equal(root.get("country").get("code"), filter.countryCode()));
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

    public static Specification<Client> exportFilter(ClientExportFilter filter) {

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
            // Active
            // IMPORTANT: use != null, NOT if (filter.getActive())
            if (filter.getActive() != null) {
                predicates.add(cb.equal(root.get("isActive"), filter.getActive()));
            }
            //status
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus().toLowerCase(Locale.ROOT)));
            }

            query.orderBy(cb.desc(root.get("createdAt")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

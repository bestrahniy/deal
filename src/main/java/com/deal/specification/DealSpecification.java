package com.deal.specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import com.deal.Dto.SearchDealFilterDto;
import com.deal.models.ContractorToRole;
import com.deal.models.Deal;
import com.deal.models.DealContractor;
import com.deal.models.DealSum;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.NoArgsConstructor;

/**
 * searching by parametr
 */
@NoArgsConstructor
public class DealSpecification {

    /**
     * return all entity where active = true
     * @return all entity where active = true
     */
    public static Specification<Deal> isActive() {
        return (root, query, cb) -> cb.equal(root.get("isActive"), true);
    }

    public static Specification<Deal> searchById(UUID id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Deal> searchByDescription(String description) {
        return (root, query, cb) -> description == null ? null : cb.equal(root.get("description"), description);
    }

    public static Specification<Deal> searchAgreementNumber(String agreementNumber) {
        return (root, query, cb) -> agreementNumber == null ? null :
            cb.like(cb.lower(root.get("agreementNumber")), "%" + agreementNumber.toLowerCase() + "%");
    }

    public static Specification<Deal> searchAgreementDate(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return null;
            }
            List<Predicate> predicates = new ArrayList<>();
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("agreementDate"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("agreementDate"), to));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Deal> searchByType(List<String> types) {
        return (root, query, cb) -> types == null || types.isEmpty() ? null :
            root.get("type").get("id").in(types);
    }

    public static Specification<Deal> searchByStatus(List<String> statuses) {
        return (root, query, cb) -> statuses == null || statuses.isEmpty() ? null :
            root.get("status").get("id").in(statuses);
    }

    public static Specification<Deal> searchBorrower(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null) {
                return null;
            }

            Join<Deal, DealContractor> contractorJoin = root.join("contractors", JoinType.INNER);
            Join<DealContractor, ContractorToRole> roleJoin = contractorJoin.join("roles", JoinType.INNER);

            Predicate isBorrower = cb.equal(roleJoin.get("role").get("category"), "BORROWER");
            Predicate searchPredicate = cb.or(
                cb.like(cb.lower(contractorJoin.get("contractorId")), "%" + searchTerm.toLowerCase() + "%"),
                cb.like(cb.lower(contractorJoin.get("name")), "%" + searchTerm.toLowerCase() + "%"),
                cb.like(cb.lower(contractorJoin.get("inn")), "%" + searchTerm.toLowerCase() + "%")
            );

            return cb.and(isBorrower, searchPredicate);
        };
    }

    public static Specification<Deal> searchSum(BigDecimal value, String currency) {
        return (root, query, cb) -> {
            if (value == null && currency == null) {
                return null;
            }

            Join<Deal, DealSum> sumJoin = root.join("sums", JoinType.INNER);
            List<Predicate> predicates = new ArrayList<>();

            if (value != null) {
                predicates.add(cb.equal(sumJoin.get("sum"), value));
            }
            if (currency != null) {
                predicates.add(cb.equal(sumJoin.get("currency").get("id"), currency));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Deal> searchWarranity(String searchTerm) {
        return (root, query, cb) -> {
            if (searchTerm == null) {
                return null;
            }

            Join<Deal, DealContractor> contractorJoin = root.join("contractors", JoinType.INNER);
            Join<DealContractor, ContractorToRole> roleJoin = contractorJoin.join("roles", JoinType.INNER);

            Predicate isWarranity = cb.equal(roleJoin.get("role").get("category"), "WARRANITY");
            Predicate searchPredicate = cb.or(
                cb.like(cb.lower(contractorJoin.get("contractorId")), "%" + searchTerm.toLowerCase() + "%"),
                cb.like(cb.lower(contractorJoin.get("name")), "%" + searchTerm.toLowerCase() + "%"),
                cb.like(cb.lower(contractorJoin.get("inn")), "%" + searchTerm.toLowerCase() + "%")
            );

            return cb.and(isWarranity, searchPredicate);
        };
    }

    /**
     * applies all filter and return deal
     * @param request dto with filter
     * @return list with deal
     */
    public static Specification<Deal> buildSearchSpecification(SearchDealFilterDto request) {
        List<Specification<Deal>> specs = new ArrayList<>();
        specs.add(isActive());
        specs.add(searchById(request.getDealId()));
        specs.add(searchByDescription(request.getDescription()));
        specs.add(searchAgreementNumber(request.getAgreementNumber()));
        specs.add(searchAgreementDate(request.getAgreementDateFrom(), request.getAgreementDateTo()));
        specs.add(searchByType(request.getDealTypes()));
        specs.add(searchByStatus(request.getStatus()));
        specs.add(searchBorrower(request.getBorrowerSearch()));
        specs.add(searchWarranity(request.getWarranitySearch()));
        specs.add(searchSum(request.getSumValue(), request.getSumCurrency()));

        Specification<Deal> result = specs.stream()
            .filter(Objects::nonNull)
            .reduce(Specification::and)
            .orElse(null);

        return result;
    }

}

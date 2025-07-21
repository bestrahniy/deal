package com.deal.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.deal.models.Deal;
import com.deal.models.DealContractor;
import com.deal.models.DealType;

public interface DealRepository extends JpaRepository<Deal, UUID>, JpaSpecificationExecutor<Deal> {

    @EntityGraph(attributePaths = {"type", "status"})
    Optional<Deal> findBaseDetailsById(UUID id);

    @Query("""
        SELECT c FROM DealContractor c
        LEFT JOIN FETCH c.contractorToRole ctr
        LEFT JOIN FETCH ctr.contractorRole
        WHERE c.deal.id = :id AND c.isActive = true
        """)
    List<DealContractor> findContractorsWithRolesByDealId(@Param("id") UUID id);

    List<Deal> findAllByType(DealType type);

}

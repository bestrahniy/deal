package com.deal.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.deal.models.ContractorRole;

public interface ContractorRoleRepository extends JpaRepository<ContractorRole, String> {

    @Query("""
        SELECT cr FROM ContractorRole cr
        LEFT JOIN FETCH cr.contractorToRoles ctr
        WHERE cr.id = :roleId
        """)
    Optional<ContractorRole> findByIdWithRoles(@Param("roleId") String roleId);

}


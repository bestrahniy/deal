package com.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deal.models.ContractorToRole;
import com.deal.models.ContractorToRoleId;

public interface ContractorToRoleRepository extends JpaRepository<ContractorToRole, ContractorToRoleId>{
    
}

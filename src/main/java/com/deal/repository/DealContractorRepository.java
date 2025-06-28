package com.deal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.deal.models.DealContractor;

public interface DealContractorRepository extends JpaRepository<DealContractor, UUID>{
    
}

package com.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deal.models.DealType;

public interface DealTypeRepository extends JpaRepository<DealType, String> {
    
}

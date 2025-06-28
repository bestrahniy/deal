package com.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deal.models.DealSum;

public interface DealSumRepository extends JpaRepository<DealSum, Long> {
    
}

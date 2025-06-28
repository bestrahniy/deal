package com.deal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.deal.models.Deal;

public interface DealRepository extends JpaRepository<Deal, UUID> {

}

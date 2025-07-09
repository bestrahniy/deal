package com.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deal.models.DealStatus;

public interface DealStatusRepository extends JpaRepository<DealStatus, String> {

}

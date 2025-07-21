package com.deal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deal.models.DealType;

public interface DealTypeRepository extends JpaRepository<DealType, String> {

    Optional<DealType> findByName(String name);

}

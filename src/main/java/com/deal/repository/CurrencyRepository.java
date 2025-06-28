package com.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deal.models.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String>{
    
}

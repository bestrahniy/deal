package com.deal.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.deal.DTO.DealSaveDto;
import com.deal.models.Deal;
import com.deal.services.DealService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/deal")
@AllArgsConstructor
public class DealControllers {
    
    private final DealService dealService;

    @PutMapping("/save")
    public ResponseEntity<Deal> saveDeal(@RequestBody DealSaveDto dealSaveDto) {
        Deal result = dealService.saveDeal(dealSaveDto);
        return ResponseEntity.ok(result);
    }
    
}

package com.deal.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.deal.DTO.DealSaveDto;
import com.deal.DTO.DealStatusDto;
import com.deal.models.Deal;
import com.deal.services.DealService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/deal")
@AllArgsConstructor
public class DealControllers {
    
    private final DealService dealService;

    @PutMapping("/save")
    public UUID saveDeal(@Valid @RequestBody DealSaveDto dealSaveDto) {
        Deal result = dealService.saveDeal(dealSaveDto);
        return result.getId();
    }
    
    @PatchMapping("/change/status")
    public void changeStatus(@RequestParam UUID id, @Valid @RequestParam DealStatusDto dealStatusDto){
        dealService.changeStatus(id, dealStatusDto);
    }
}

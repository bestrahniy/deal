package com.deal.services;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deal.DTO.DealSaveDto;
import com.deal.models.Currency;
import com.deal.models.Deal;
import com.deal.models.DealContractor;
import com.deal.models.DealSum;
import com.deal.repository.ContractorToRoleRepository;
import com.deal.repository.ContrantorRoleRepository;
import com.deal.repository.CurrencyRepository;
import com.deal.repository.DealContractorRepository;
import com.deal.repository.DealRepository;
import com.deal.repository.DealStatusRepository;
import com.deal.repository.DealSumRepository;
import com.deal.repository.DealTypeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DealService {
    
    private final ContractorToRoleRepository contractorToRoleRepository;
    
    private final ContrantorRoleRepository conractorRoleRepository;

    private final CurrencyRepository currencyRepository;

    private final DealContractorRepository dealContractorRepository;

    private final DealRepository dealRepository;

    private final DealStatusRepository dealStatusRepository;

    private final DealSumRepository dealSumRepository;

    private final DealTypeRepository dealTypeRepository;


    @Transactional
    public Deal saveDeal(DealSaveDto dealSaveDto){

        Deal deal = new Deal();

        deal.setId(dealSaveDto.getId());
        deal.setDescription(dealSaveDto.getDescription());
        deal.setAgreementNumber(dealSaveDto.getAgreementNumber());
        deal.setAgreementDate(dealSaveDto.getAgreementDate());
        deal.setAgreementStartDt(dealSaveDto.getAgreementStartDt());
        deal.setAvailabilityDate(dealSaveDto.getAvailabilityDate());

        deal.setType(dealTypeRepository
            .findById(dealSaveDto.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException("type not found"))
        );

        deal.setCloseDt(dealSaveDto.getCloseDt());

        deal.setSums(
            dealSaveDto.getSums().stream()
                .map(sum ->{
                    DealSum dealSum = new DealSum();
                    dealSum.setId(sum.getId());
                    dealSum.setDeal(deal);
                    dealSum.setMain(sum.isMain());
                    Currency currency = currencyRepository.findById(sum.getCurrencyId())
                        .orElseThrow(() -> new EntityNotFoundException()
                    );
                    dealSum.setCurrency(currency);
                    dealSum.setSum(BigDecimal.valueOf(sum.getSum()));
                    
                    return dealSum;
                })
                .collect(Collectors.toList())
        );
        
        deal.setContractors(
            dealSaveDto.getContractors().stream()
                .map(contractor -> {
                    DealContractor dealContractor = new DealContractor();
                    dealContractor.setId(contractor.getId());
                    dealContractor.setDeal(deal);
                    dealContractor.setContractorId(contractor.getContractorId());
                    dealContractor.setName(contractor.getName());
                    dealContractor.setInn(contractor.getInn());
                    dealContractor.setMain(contractor.isMain());
                    
                    return dealContractor;
                })
                .collect(Collectors.toList())
        );

    return dealRepository.save(deal);
    }
}

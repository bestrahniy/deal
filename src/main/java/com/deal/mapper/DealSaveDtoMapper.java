package com.deal.mapper;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.deal.Dto.DealSaveDto;
import com.deal.models.Currency;
import com.deal.models.Deal;
import com.deal.models.DealContractor;
import com.deal.models.DealSum;
import com.deal.repository.CurrencyRepository;
import com.deal.repository.DealRepository;
import com.deal.repository.DealStatusRepository;
import com.deal.repository.DealTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

/**
 * mapper for save new deal
 */
@Component
@AllArgsConstructor
public class DealSaveDtoMapper {

    private final CurrencyRepository currencyRepository;

    private final DealRepository dealRepository;

    private final DealStatusRepository dealStatusRepository;

    private final DealTypeRepository dealTypeRepository;

    /**
     * mapper for update or create new deal and send it to service
     * @param dealSaveDto dto with data
     * @return deal
     */
    public Deal convertToEntity(DealSaveDto dealSaveDto) {
        Deal deal;
        if (dealSaveDto.getId() != null) {
            deal = dealRepository.findById(dealSaveDto.getId())
                .orElseThrow(() ->  new EntityNotFoundException("Deal not found"));
        } else {
            deal = new Deal();
            deal.setStatus(dealStatusRepository.findById("DRAFT")
                .orElseThrow(() -> new EntityNotFoundException("Status not found ")));
        }

        deal.setDescription(dealSaveDto.getDescription());
        deal.setAgreementNumber(dealSaveDto.getAgreementNumber());
        deal.setAgreementDate(dealSaveDto.getAgreementDate());
        deal.setAgreementStartDt(dealSaveDto.getAgreementStartDt());
        deal.setAvailabilityDate(dealSaveDto.getAvailabilityDate());

        deal.setType(dealTypeRepository
            .findById(dealSaveDto.getTypeId())
                .orElseThrow()
        );

        deal.setSums(
            dealSaveDto.getSums().stream()
                .map(sum -> {
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
                    dealContractor.setDeal(deal);
                    dealContractor.setContractorId(contractor.getContractorId());
                    dealContractor.setName(contractor.getName());
                    dealContractor.setInn(contractor.getInn());
                    dealContractor.setMain(contractor.isMain());

                    return dealContractor;
                })
                .collect(Collectors.toList())
        );

        return deal;
    }

}

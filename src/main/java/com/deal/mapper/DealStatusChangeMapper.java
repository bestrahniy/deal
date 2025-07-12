package com.deal.mapper;

import org.springframework.stereotype.Component;
import com.deal.Dto.DealStatusDto;
import com.deal.models.Deal;
import com.deal.repository.DealRepository;
import com.deal.repository.DealStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

/**
 * mapper for schange status deal
 */
@Component
@AllArgsConstructor
public class DealStatusChangeMapper {

    private final DealRepository dealRepository;

    private final DealStatusRepository dealStatusRepository;

    /**
     * change status and send deal to service for save it in db
     * @param dealStatusDto dto with data
     * @return deal
     */
    public Deal changeStatus(DealStatusDto dealStatusDto) {
        Deal deal = dealRepository.findById(dealStatusDto.getDealId())
            .orElseThrow(() -> new EntityNotFoundException("Deal not found"));
        deal.setStatus(
            dealStatusRepository.findById(dealStatusDto.getStatusId())
                .orElseThrow(() -> new EntityNotFoundException("deal status not found"))
        );
        return deal;
    }

}

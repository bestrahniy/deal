package com.deal.services;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.deal.Dto.DealSaveDto;
import com.deal.Dto.DealStatusDto;
import com.deal.Dto.ResponseDealByIdDto;
import com.deal.Dto.SearchDealFilterDto;
import com.deal.mapper.DealSaveDtoMapper;
import com.deal.mapper.DealStatusChangeMapper;
import com.deal.mapper.ResponseDealByIdMapper;
import com.deal.models.Deal;
import com.deal.models.DealContractor;
import com.deal.repository.DealRepository;
import com.deal.specification.DealSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

/**
 * service for working with deal entity
 */
@Service
@AllArgsConstructor
public class DealService {

    private final  DealSaveDtoMapper dealSaveDtoMapper;

    private final DealStatusChangeMapper dealStatusChangeMapper;

    private final DealRepository dealRepository;

    private final ResponseDealByIdMapper responseDealByIdMapper;

    /**
     * save a new deal
     * @param dealSaveDto dto with data
     * @return deal entity
     */
    @Transactional
    public Deal saveDeal(DealSaveDto dealSaveDto) {
        Deal deal = dealSaveDtoMapper.convertToEntity(dealSaveDto);
        dealRepository.save(deal);
        return deal;
    }

    /**
     * change deal status
     * @param dealStatusDto dto with data
     * @return save deal
     */
    @Transactional
    public Deal changeStatus(DealStatusDto dealStatusDto) {
        Deal deal = dealStatusChangeMapper.changeStatus(dealStatusDto);
        return dealRepository.save(deal);
    }

    /**
     * provides related with method for filling dto for showing deal on screen
     * @param id unique of deal
     * @return link on method for filling dto for showing deal on screen
     */
    @Transactional
    public ResponseDealByIdDto responseDealById(UUID id) {
        Deal deal = dealRepository.findBaseDetailsById(id)
            .orElseThrow(() -> new EntityNotFoundException("Deal not found"));

        List<DealContractor> contractors = dealRepository.findContractorsWithRolesByDealId(id);
        deal.setContractors(contractors);

        return responseDealByIdMapper.responseDeal(deal);
    }

    /**
     * search all deal by filter and using pagination
     * @param dto with filter
     * @return several deal by page
     */
    @Transactional
    public Page<ResponseDealByIdDto> searchDeals(SearchDealFilterDto dto) {
        Specification<Deal> specification = DealSpecification.buildSearchSpecification(dto);
        Page<Deal> dealPage = dealRepository.findAll(specification, dto.getPageable());
        return dealPage.map(deal -> {
                List<DealContractor> contractors = dealRepository.findContractorsWithRolesByDealId(deal.getId());
                deal.setContractors(contractors);
                return responseDealByIdMapper.responseDeal(deal);
            });
    }

}

package com.deal.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.deal.Dto.DealSaveDto;
import com.deal.Dto.DealStatusDto;
import com.deal.Dto.ResponseDealByIdDto;
import com.deal.Dto.SearchDealFilterDto;
import com.deal.mapper.DealSaveDtoMapper;
import com.deal.mapper.DealStatusChangeMapper;
import com.deal.mapper.ResponseDealByIdMapper;
import com.deal.models.Deal;
import com.deal.models.DealStatus;
import com.deal.repository.DealRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DealServicesTest {
@Mock
    private DealRepository dealRepository;

    @Mock
    private DealSaveDtoMapper dealSaveDtoMapper;

    @Mock
    private DealStatusChangeMapper dealStatusChangeMapper;

    @Mock
    private ResponseDealByIdMapper responseDealByIdMapper;

    @InjectMocks
    private DealService dealService;

    @Test
    void saveDealTest() {
        DealSaveDto dto = new DealSaveDto();
        Deal deal = new Deal();
        deal.setId(UUID.randomUUID());

        when(dealSaveDtoMapper.convertToEntity(dto)).thenReturn(deal);
        when(dealRepository.save(deal)).thenReturn(deal);

        Deal result = dealService.saveDeal(dto);

        assertNotNull(result);
        assertEquals(deal.getId(), result.getId());
        verify(dealRepository).save(deal);
    }

    @Test
    void changeStatusTest() {
        DealStatusDto dto = new DealStatusDto();
        dto.setDealId(UUID.randomUUID());
        dto.setStatusId("ACTIVE");

        Deal deal = new Deal();
        DealStatus newStatus = new DealStatus();
        newStatus.setId("ACTIVE");

        when(dealStatusChangeMapper.changeStatus(dto)).thenReturn(deal);
        when(dealRepository.save(deal)).thenReturn(deal);

        Deal result = dealService.changeStatus(dto);

        assertNotNull(result);
        verify(dealRepository).save(deal);
    }

    @Test
    void responseDealByIdTest() {
        UUID dealId = UUID.randomUUID();
        Deal deal = new Deal();
        deal.setId(dealId);

        ResponseDealByIdDto expectedDto = new ResponseDealByIdDto();
        expectedDto.setId(dealId);

        when(dealRepository.findBaseDetailsById(dealId))
            .thenReturn(Optional.of(deal));
        when(dealRepository.findContractorsWithRolesByDealId(dealId))
            .thenReturn(Collections.emptyList());
        when(responseDealByIdMapper.responseDeal(deal))
            .thenReturn(expectedDto);

        ResponseDealByIdDto result = dealService.responseDealById(dealId);

        assertNotNull(result);
        assertEquals(dealId, result.getId());
    }

    @Test
    void searchDealsTest() {
        SearchDealFilterDto filter = new SearchDealFilterDto();
        filter.setDescription("test");
        filter.setPageable(PageRequest.of(0, 10));

        Deal deal = new Deal();
        Page<Deal> dealPage = new PageImpl<>(List.of(deal));
        ResponseDealByIdDto dto = new ResponseDealByIdDto();

        when(dealRepository.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(dealPage);
        when(dealRepository.findContractorsWithRolesByDealId(any()))
            .thenReturn(Collections.emptyList());
        when(responseDealByIdMapper.responseDeal(any()))
            .thenReturn(dto);

        Page<ResponseDealByIdDto> result = dealService.searchDeals(filter);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(dealRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void searchDealsByAllFilters() {
        SearchDealFilterDto filter = new SearchDealFilterDto();
        filter.setDealId(UUID.randomUUID());
        filter.setDescription("test");
        filter.setAgreementNumber("123");
        filter.setAgreementDateFrom(LocalDate.now());
        filter.setAgreementDateTo(LocalDate.now());
        filter.setDealTypes(List.of("CREDIT"));
        filter.setStatus(List.of("ACTIVE"));
        filter.setBorrowerSearch("borrower");
        filter.setWarranitySearch("warranity");
        filter.setSumValue(BigDecimal.valueOf(1000));
        filter.setSumCurrency("RUB");
        filter.setPageable(PageRequest.of(0, 10));

        Page<Deal> emptyPage = new PageImpl<>(Collections.emptyList());
        when(dealRepository.findAll(any(Specification.class), any(Pageable.class)))
            .thenReturn(emptyPage);

        Page<ResponseDealByIdDto> result = dealService.searchDeals(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

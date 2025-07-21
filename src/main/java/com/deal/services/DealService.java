package com.deal.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.deal.Dto.DealSaveDto;
import com.deal.Dto.DealStatusDto;
import com.deal.Dto.GetFullDealDto;
import com.deal.Dto.ResponseDealByIdDto;
import com.deal.Dto.SearchDealFilterDto;
import com.deal.exception.MissingUserRights;
import com.deal.mapper.DealSaveDtoMapper;
import com.deal.mapper.DealStatusChangeMapper;
import com.deal.mapper.GetFullDealDtoMapper;
import com.deal.mapper.ResponseDealByIdMapper;
import com.deal.models.Deal;
import com.deal.models.DealContractor;
import com.deal.models.DealType;
import com.deal.repository.ContractorRoleRepository;
import com.deal.repository.DealRepository;
import com.deal.repository.DealTypeRepository;
import com.deal.specification.DealSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.Authentication;

/**
 * service for working with deal entity
 */
@Service
@AllArgsConstructor
public class DealService {

    private final ContractorRoleRepository contractorRoleRepository;

    private final  DealSaveDtoMapper dealSaveDtoMapper;

    private final DealStatusChangeMapper dealStatusChangeMapper;

    private final DealRepository dealRepository;

    private final ResponseDealByIdMapper responseDealByIdMapper;

    private final DealTypeRepository dealTypeRepository;

    private final GetFullDealDtoMapper getFullDealDtoMapper;

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
    @PostFilter("""
            (!hasRole("CREDIT_USER") or filterObject.type.id == 'CREDIT') and
            (!hasRole("OVERDRAFT_USER") or filterObject.type.id == 'OVERDRAFT')
            """)
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

        boolean creditUserRoleExists = SecurityContextHolder.getContext().getAuthentication()
            .getAuthorities().stream()
                .anyMatch(roles -> roles.getAuthority().equals("ROLE_CREDIT_USER"));

        boolean overdraftUserRoleExists = SecurityContextHolder.getContext().getAuthentication()
            .getAuthorities().stream()
                .anyMatch(roles -> roles.getAuthority().equals("ROLE_OVERDRAFT_USER"));

        if (creditUserRoleExists) {
            specification = specification.and((root, query, cb) ->
                cb.equal(root.get("type").get("id"), "CREDIT"));
        }

        if (overdraftUserRoleExists) {
            specification = specification.and((root, query, cb) ->
                cb.equal(root.get("type").get("id"), "OVERDRAFT"));
        }

        Page<Deal> dealPage = dealRepository.findAll(specification, dto.getPageable());
        return dealPage.map(deal -> {
                List<DealContractor> contractors = dealRepository.findContractorsWithRolesByDealId(deal.getId());
                deal.setContractors(contractors);
                return responseDealByIdMapper.responseDeal(deal);
            });
    }

    public List<GetFullDealDto> findDealForSpecificUser(Collection<? extends GrantedAuthority> roles) {
        if (hasRole(roles, "ROLE_CREDIT_USER")) {
            return dealRepository.findAllByType(
                    dealTypeRepository.findByName("CREDIT")
                        .orElseThrow(() -> new EntityNotFoundException("entity not found"))
                ).stream()
                    .map(deal -> {
                        GetFullDealDto dto = getFullDealDtoMapper.dtoMapper(deal);
                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        if (hasRole(roles, "ROLE_CREDIT_USER")) {
            DealType credit = dealTypeRepository.findByName("CREDIT")
                .orElseThrow(() -> new EntityNotFoundException("entity not found"));
            return dealRepository.findAllByType(credit).stream()
                        .map(getFullDealDtoMapper::dtoMapper)
                        .collect(Collectors.toList());
        }

        if (hasRole(roles, "ROLE_OVERDRAFT_USER")) {
            DealType od = dealTypeRepository.findByName("OVERDRAFT")
                .orElseThrow(() -> new EntityNotFoundException("entity not found"));
            return dealRepository.findAllByType(od).stream()
                        .map(getFullDealDtoMapper::dtoMapper)
                        .collect(Collectors.toList());
        }

        return dealRepository.findAll().stream()
            .map(getFullDealDtoMapper::dtoMapper)
            .collect(Collectors.toList());
    }

    public boolean hasRole(Collection<? extends GrantedAuthority> roles, String role) {
        return roles.stream()
            .anyMatch(getRole -> getRole.getAuthority().equals(role));
    }

    /**
     * return id deal and in statusId variable of DealStatusDto
     * put status name
     * @param deal entity
     * @return DealStatusDto
     */
    @PostFilter("""
            (!hasRole("CREDIT_USER") or filterObject.type.id == 'CREDIT') and
            (!hasRole("OVERDRAFT_USER") or filterObject.type.id == 'OVERDRAFT')
            """)
    public DealStatusDto getStatus(Deal deal) {
        return DealStatusDto.builder()
            .dealId(deal.getId())
            .statusId(deal.getStatus().getName())
            .build();
    }

    /**
     * get all roles from token, if user is authorized
     * else custom exception
     * @param login login user
     * @return set roles
     */
    public Set<String> getAllUserRoles(String login) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!login.equals(authentication.getPrincipal())) {
            throw new MissingUserRights(login);
        }
        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(role -> role.startsWith("ROLE_")
                ? role.substring("ROLE_".length())
                : role)
            .collect(Collectors.toSet());
    }

}

package com.deal.services;

import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.deal.Dto.AddNewRoleDto;
import com.deal.Dto.DealContractorSaveDto;
import com.deal.Dto.LogicalDeleteRoleDto;
import com.deal.mapper.AddNewRoleMapper;
import com.deal.mapper.DealContractorSaveMapper;
import com.deal.models.DealContractor;
import com.deal.repository.DealContractorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

/**
 * secvice for working with dealContractor entity
 */
@Service
@AllArgsConstructor
public class ContractorService {

    private final DealContractorRepository dealContractorRepository;

    private final DealContractorSaveMapper dealContractorSaveMapper;

    private final AddNewRoleMapper addNewRoleMapper;

    private final JdbcTemplate jdbcTemplate;

    /**
     * save new dealContractor
     * @param contractor dto with new dealContractor
     * @return save new dealContractor
     */
    @Transactional
    public DealContractor saveDealContractor(DealContractorSaveDto contractor) {
        DealContractor dealContractor = dealContractorSaveMapper.saveDeal(contractor);
        return dealContractorRepository.save(dealContractor);
    }

    /**
     * logical delete dealContractor
     * @param id unique id of dealContractor
     * @return save dealContractor
     */
    @Transactional
    public DealContractor deleteDealContractor(UUID id) {
        DealContractor dealContractor = dealContractorRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("deal contractor not found"));
        dealContractor.setActive(false);
        return dealContractorRepository.save(dealContractor);
    }

    /**
     * add new role to dealContractor
     * @param addNewRoleDto dto with data for add new role
     * @return save dealContractor
     */
    @Transactional
    public DealContractor addNewRole(AddNewRoleDto addNewRoleDto) {
        DealContractor dealContractor = addNewRoleMapper.addNewRole(addNewRoleDto);
        return dealContractorRepository.save(dealContractor);
    }

    /**
     * logical delete role to dealContractor
     * @param dto with data
     */
    @Transactional
    public void deleteRole(LogicalDeleteRoleDto dto) {
        jdbcTemplate.update("""
            UPDATE contractor_to_role SET is_active = false WHERE
            role_id = ? and contractor_id = ?
            """, dto.getRoleId(), dto.getDealContractorId());
    }

}

package com.deal.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.deal.Dto.AddNewRoleDto;
import com.deal.models.ContractorToRole;
import com.deal.models.ContractorToRoleId;
import com.deal.models.DealContractor;
import com.deal.repository.ContractorRoleRepository;
import com.deal.repository.DealContractorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

/**
 * mapper for add new role to deal contractor
 */
@Component
@AllArgsConstructor
public class AddNewRoleMapper {

    private final DealContractorRepository dealContractorRepository;

    private final ContractorRoleRepository contractorRoleRepository;

    /**
     * find deal contractor and add his new role
     * @param dto with data
     * @return dealContractor
     */
    public DealContractor addNewRole(AddNewRoleDto dto) {
        DealContractor dealContractor = dealContractorRepository.findById(dto.getDealContractorId())
            .orElseThrow(() -> new EntityNotFoundException("deal contractor not found"));

        List<ContractorToRole> roles = dealContractor.getContractorToRole();
        ContractorToRole contractorToRole = new ContractorToRole();
        contractorToRole.setContractorToRoleId(
                new ContractorToRoleId(
                    dto.getDealContractorId(),
                    dto.getRoleId())
        );
        contractorToRole.setDealContractor(dealContractor);
        contractorToRole.setContractorRole(
            contractorRoleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new EntityNotFoundException("role not found"))
        );

        boolean roleAlreadyExists = dealContractor.getContractorToRole().stream()
            .anyMatch(existingRole ->
                existingRole.getContractorToRoleId().getRoleId().equals(dto.getRoleId()) &&
                existingRole.isActive()
            );

        if (!roleAlreadyExists) {
            roles.add(contractorToRole);
        } else {
            throw new IllegalArgumentException("role already add");
        }

        dealContractor.setContractorToRole(roles);

        return dealContractor;
    }

}

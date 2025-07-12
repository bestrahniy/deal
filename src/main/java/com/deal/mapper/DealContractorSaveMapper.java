package com.deal.mapper;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.deal.Dto.DealContractorSaveDto;
import com.deal.models.ContractorToRole;
import com.deal.models.ContractorToRoleId;
import com.deal.models.DealContractor;
import com.deal.repository.ContractorRoleRepository;
import com.deal.repository.DealContractorRepository;
import com.deal.repository.DealRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

/**
 * mapper for save new deal contractor
 */
@Component
@AllArgsConstructor
public class DealContractorSaveMapper {

    private final ContractorRoleRepository contractorRoleRepository;

    private final DealRepository dealRepository;

    private final DealContractorRepository dealContractorRepository;

    /**
     * create a new dealContractor and send it in service
     * @param dealContractorSaveDto dto with data
     * @return dealContractor
     */
    public DealContractor saveDeal(DealContractorSaveDto dealContractorSaveDto) {
        DealContractor dealContractor;
        if (dealContractorSaveDto.getId() != null) {
            dealContractor = dealContractorRepository.findById(dealContractorSaveDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("deal contractor not found"));
        } else {
            dealContractor = new DealContractor();
        }

        dealContractor.setDeal(
            dealRepository.findById(dealContractorSaveDto.getDealId())
                .orElseThrow(() -> new EntityNotFoundException("Deal not found"))
        );

        dealContractor.setName(dealContractorSaveDto.getName());
        dealContractor.setInn(dealContractorSaveDto.getInn());
        dealContractor.setMain(dealContractorSaveDto.isMain());

        dealContractor.setContractorToRole(
            dealContractorSaveDto.getRoleIds().stream()
                .map(
                    contractorRoleId -> {
                        ContractorToRole ctrRole = new ContractorToRole();

                        ctrRole.setContractorToRoleId(new ContractorToRoleId(
                            dealContractor.getId(),
                            contractorRoleId
                        ));

                        ctrRole.setDealContractor(dealContractor);

                        ctrRole.setContractorRole(
                            contractorRoleRepository.findByIdWithRoles(contractorRoleId)
                                .orElseThrow(() -> new EntityNotFoundException("contracto role not fond"))
                        );

                        return ctrRole;
                    }
                )
                .collect(Collectors.toList())
        );

        return dealContractor;
    }

}


package com.deal.mapper;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.deal.DealApplication;
import com.deal.Dto.ResponseDealByIdDto;
import com.deal.models.ContractorToRole;
import com.deal.models.Deal;
import com.deal.models.DealContractor;
import lombok.AllArgsConstructor;

/**
 * mapper for dto to custom show Deal on screen
 */
@Component
@AllArgsConstructor
public class ResponseDealByIdMapper {

    private final DealApplication dealApplication;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        .withZone(ZoneId.systemDefault());

    /**
     * create a dto with necessary data
     * @param deal entity
     * @return ready dto
     */
    public ResponseDealByIdDto responseDeal(Deal deal) {
        ResponseDealByIdDto dto = new ResponseDealByIdDto();
        dto.setId(deal.getId());
        dto.setDescription(deal.getDescription());
        dto.setAgreementNumber(deal.getAgreementNumber());
        dto.setAgreementDate(dateFormatter.format(deal.getAgreementDate()));
        dto.setAgreementStartDt(dateTimeFormatter.format(deal.getAgreementStartDt()));
        dto.setAvailabilityDate(dateFormatter.format(deal.getAvailabilityDate()));
        if (deal.getCloseDt() != null) {
            dto.setCloseDate(dateTimeFormatter.format(deal.getCloseDt()));
        }

        dto.setType(
            new ResponseDealByIdDto.DealTypeDto(
                deal.getType().getId(),
                deal.getType().getName()
            )
        );

        dto.setStatus(
            new ResponseDealByIdDto.DealStatusDto(
                deal.getStatus().getId(),
                deal.getStatus().getName()
            )
        );

        dto.setContractors(
            deal.getContractors().stream()
                .filter(DealContractor::isActive)
                .map(contractor -> {
                    List<ResponseDealByIdDto.RoleDto> roles = contractor.getContractorToRole().stream()
                        .filter(ContractorToRole::isActive)
                        .map(role -> new ResponseDealByIdDto.RoleDto(
                            role.getContractorRole().getId(),
                            role.getContractorRole().getName(),
                            role.getContractorRole().getCategory()
                        ))
                        .collect(Collectors.toList());

                        return new ResponseDealByIdDto.ContractorDto(
                            contractor.getId(),
                            contractor.getContractorId(),
                            contractor.getName(),
                            contractor.isMain(),
                            roles
                        );
                })
                .collect(Collectors.toList())
        );

        return dto;
    }

}

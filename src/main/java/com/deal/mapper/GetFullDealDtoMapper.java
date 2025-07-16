package com.deal.mapper;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.deal.Dto.GetFullDealDto;
import com.deal.models.Deal;

@Component
public class GetFullDealDtoMapper {

    public GetFullDealDto dtoMapper(Deal deal) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            .withZone(ZoneId.systemDefault());

        GetFullDealDto.GetFullDealType typeDto = new GetFullDealDto.GetFullDealType();
        typeDto.setId(deal.getType().getId());
        typeDto.setName(deal.getType().getName());

        GetFullDealDto.GetFullDealStatus statusDto = new GetFullDealDto.GetFullDealStatus();
        statusDto.setId(deal.getStatus().getId());
        statusDto.setName(deal.getStatus().getName());

        return GetFullDealDto.builder()
            .id(deal.getId())
            .description(deal.getDescription())
            .agreementNumber(deal.getAgreementNumber())
            .agreementDate(deal.getAgreementDate().format(format))
            .agreementStartDt(format.format(deal.getAgreementStartDt()))
            .type(List.of(typeDto))
            .status(List.of(statusDto))
            .sum(
                deal.getSums().stream()
                    .map(sum -> {
                        GetFullDealDto.GetFulldDealSum dto = new GetFullDealDto.GetFulldDealSum();
                        dto.setValue((sum.getSum().intValue()));
                        dto.setCurrency(sum.getCurrency().getName());
                        return dto;
                    })
                    .collect(Collectors.toList())
            )
            .build();
    }

}

package com.deal.Dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDealByIdDto {

    private UUID id;
    private String description;
    private String agreement_number;
    private String agreement_date;
    private String agreement_start_dt;
    private String availability_date;
    private DealTypeDto type;
    private DealStatusDto status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DealTypeDto {
        private String id;
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DealStatusDto {
        private String id;
        private String name;
    }

    private String close_date;

    private List<ContractorDto> contractors;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ContractorDto {
        private UUID id;
        private String contractorId;
        private String name;
        private boolean main;
        private List<RoleDto> roles;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDto {
        private String id;
        private String name;
        private String category;
    }

}

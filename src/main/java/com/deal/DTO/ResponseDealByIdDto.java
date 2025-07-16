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
    private String agreementNumber;
    private String agreementDate;
    private String agreementStartDt;
    private String availabilityDate;
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

    private String closeDate;

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

package com.deal.Dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetFullDealDto {

    private UUID id;

    private String description;

    private String agreementNumber;

    private String agreementDate;

    private String agreementStartDt;

    private String aviailabilityDate;

    private List<GetFullDealType> type;

    private List<GetFullDealStatus> status;

    private List<GetFulldDealSum> sum;

    private Instant closeDt;

    @Data
    public static class GetFullDealType {
        private String id;
        private String name;
    }

    @Data
    public static class GetFullDealStatus {
        private String id;
        private String name;
    }

    @Data
    public static class GetFulldDealSum {
        private Integer value;
        private String currency;
    }

}

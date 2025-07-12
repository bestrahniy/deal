package com.deal.Dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealSaveDto {

    @Schema(accessMode = Schema.AccessMode.READ_WRITE)
    private UUID id;

    private String description;

    private String agreementNumber;

    private LocalDate agreementDate;

    private Instant agreementStartDt;

    private LocalDate availabilityDate;

    private String typeId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String status;

    private List<DealSumDto> sums;

    private List<DealContractorDto> contractors;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DealSumDto {

        @Schema(accessMode = Schema.AccessMode.READ_ONLY)
        private Long id;

        private float sum;

        private String currencyId;

        private boolean isMain;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DealContractorDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    private String contractorId;

    private String name;

    private String inn;

    private boolean isMain;
    }

}

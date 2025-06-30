package com.deal.DTO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DealSaveDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
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
    
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime modifyDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String createUserId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String modifyUserId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean isActive = true;

}

package com.deal.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DealSumDto {
    
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    private float sum;

    private String currencyId;
    
    private boolean isMain;
}

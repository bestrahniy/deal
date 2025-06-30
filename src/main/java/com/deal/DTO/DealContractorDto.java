package com.deal.DTO;

import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DealContractorDto {
    
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    private String contractorId;

    private String name;

    private String inn;

    private boolean isMain;

}

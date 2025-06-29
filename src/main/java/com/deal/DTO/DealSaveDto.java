package com.deal.DTO;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class DealSaveDto {

    private UUID id;

    private String description;
    
    private String agreementNumber;
    
    private LocalDate agreementDate;
    
    private Instant agreementStartDt;
    
    private LocalDate availabilityDate;
    
    private String typeId;
    
    private Instant closeDt;

    private List<DealSumDto> sums;

    private List<DealContractorDto> contractors;
}

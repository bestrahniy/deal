package com.deal.DTO;

import java.util.UUID;

import lombok.Data;

@Data
public class DealContractorDto {
    
    private UUID id;

    private String contractorId;

    private String name;

    private String inn;

    private boolean isMain;

}

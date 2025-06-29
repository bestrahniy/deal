package com.deal.DTO;

import lombok.Data;

@Data
public class DealSumDto {
    
    private Long id;

    private float sum;

    private String currencyId;
    
    private boolean isMain;
}

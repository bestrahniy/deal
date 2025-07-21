package com.deal.Dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DealStatusDto {

    private UUID dealId;

    private String statusId;

}

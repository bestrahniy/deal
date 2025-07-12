package com.deal.Dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogicalDeleteRoleDto {

    private UUID dealContractorId;

    private String roleId;

}

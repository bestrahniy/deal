package com.deal.Dto;

import java.util.List;
import java.util.UUID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealContractorSaveDto {

    @Schema(accessMode = Schema.AccessMode.READ_WRITE)
    private UUID id;

    private UUID dealId;

    private String name;

    private String inn;

    private boolean isMain;

    private List<String> roleIds;

}

package com.deal.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.deal.Dto.DealContractorSaveDto;
import com.deal.services.ContractorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
    name = "deal contractor api",
    description = "manage all deal contractor and related entity"
)
@RestController
@RequestMapping("/deal-contractor")
@AllArgsConstructor
public class DealContractorControllers {

    private final ContractorService contractorService;

    @Operation(
        summary = "save a new deal contractor",
        description = """
                save a new deal contractor use the
                dealCnractorSaveDto
                """,
        responses = {
            @ApiResponse(responseCode = "201", description = "created a new deal conractor"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PutMapping("/save")
    public ResponseEntity<?> saveContractor(@RequestBody DealContractorSaveDto dealContractorSaveDto) {
        return ResponseEntity.ok(contractorService.saveDealContractor(dealContractorSaveDto));
    }

    @Operation(
        summary = "logical delete deal contractor",
        description = "variable is_active become false",
        responses = {
            @ApiResponse(responseCode = "204", description = "logical delete is successfully"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDealContractor(
        @Parameter(
            description = "unique id of deal contractor",
            example = "9afde06e-4c84-4b2c-b648-e88725e9aa28"
        )
        @PathVariable UUID id) {
        contractorService.deleteDealContractor(id);
        return ResponseEntity.noContent().build();
    }

}

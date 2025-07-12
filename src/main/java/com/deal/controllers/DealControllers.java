package com.deal.controllers;

import com.deal.Dto.DealSaveDto;
import com.deal.Dto.DealStatusDto;
import com.deal.Dto.ResponseDealByIdDto;
import com.deal.Dto.SearchDealFilterDto;
import com.deal.models.Deal;
import com.deal.services.DealExcelExporterService;
import com.deal.services.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.UUID;

@Tag(
    name = "controller for deal",
    description = "manage all deal and related entity"
)

@RestController
@RequestMapping("/deal")
@AllArgsConstructor
public class DealControllers {

    private final DealService dealService;

    private final DealExcelExporterService dealExcelExporterService;

    @Operation(
        summary = "save a new deal",
        description = """
                save a new deal and all related entity
                use DealSaveDto for save
                """,
        responses = {
            @ApiResponse(responseCode = "201", description = "created a new deal"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PutMapping("/save")
    public UUID saveDeal(@Valid @RequestBody DealSaveDto dealSaveDto) {
        Deal result = dealService.saveDeal(dealSaveDto);
        return result.getId();
    }

    @Operation(
        summary = "change deal status",
        description = """
                change a deal status:
                available status:
                - DRAFT
                - ACTIVE
                - CLOSED
                """,
        responses = {
            @ApiResponse(responseCode = "204", description = "change status is success"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PatchMapping("/change/status")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody DealStatusDto dealStatusDto) {
        dealService.changeStatus(dealStatusDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "get deal by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "deal showed on screen"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDealByIdDto> responseDeal(@PathVariable UUID id) {
        return ResponseEntity.ok(dealService.responseDealById(id));
    }

    @Operation(
        summary = "search all deal by custom filter",
        responses = {
            @ApiResponse(responseCode = "200", description = "deals shows on screen"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PostMapping("/search")
    public ResponseEntity<Page<ResponseDealByIdDto>> searchDeals(
        @RequestBody SearchDealFilterDto dto,
        @PageableDefault() Pageable pageable
    ) {
        dto.setPageable(pageable);
        return ResponseEntity.ok(dealService.searchDeals(dto));
    }

    @PostMapping("/export")
    public ResponseEntity<Resource> exportDeals(
        @RequestBody SearchDealFilterDto dto,
        @PageableDefault() Pageable pageable
    ) throws IOException {

        Page<ResponseDealByIdDto> page = dealService.searchDeals(dto);
        Resource file = dealExcelExporterService.exportDeals(page);

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=deals.xlsx")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }

}

package com.deal.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.deal.services.AuthConnect;
import com.deal.services.DealExcelExporterService;
import com.deal.services.DealService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.deal.Dto.DealSaveDto;
import com.deal.Dto.DealStatusDto;
import com.deal.Dto.GetFullDealDto;
import com.deal.Dto.ResponseDealByIdDto;
import com.deal.Dto.SearchDealFilterDto;
import com.deal.Dto.UserRolesDto;
import com.deal.models.Deal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Tag(
    name = "controller for autherisate user"
)
@RestController
@AllArgsConstructor
@RequestMapping("/ui")
public class DealUiController {

    private final DealService dealService;

    private final DealExcelExporterService dealExcelExporterService;

    private final AuthConnect authConnect;

    @Operation(
        summary = "method for conntected with auth service",
        description = """
                this is where he addresses auth service, getting
                jwt token
                """,
        responses = {
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @GetMapping("/deals")
    public List<GetFullDealDto> getAuthorisateUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user = authentication.getName();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        return dealService.findDealForSpecificUser(roles);
    }

    @Operation(
        summary = "method for checking status deal",
        responses = {
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @GetMapping("/deal/deal-status")
    public ResponseEntity<?> getDealStatus(@RequestBody Deal deal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(dealService.getStatus(deal));
    }

    @Operation(
        summary = "save a new deal",
        description = """
                save a new deal and all related entity
                use DealSaveDto for save
                """,
        responses = {
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PutMapping("/deal/save")
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
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PatchMapping("/deal/change/status")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody DealStatusDto dealStatusDto) {
        dealService.changeStatus(dealStatusDto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "get deal by id",
        responses = {
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @GetMapping("/deal/{id}")
    public ResponseEntity<ResponseDealByIdDto> responseDeal(@PathVariable UUID id) {
        return ResponseEntity.ok(dealService.responseDealById(id));
    }

    @Operation(
        summary = "search all deal by custom filter",
        responses = {
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PostMapping("/deal/search")
    public ResponseEntity<Page<ResponseDealByIdDto>> searchDeals(
        @RequestBody SearchDealFilterDto dto,
        @PageableDefault() Pageable pageable
    ) {
        dto.setPageable(pageable);
        return ResponseEntity.ok(dealService.searchDeals(dto));
    }

    @Operation(
        summary = "search all deal and export their in exls file",
        responses = {
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PostMapping("/deal/search/export")
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

    @Operation(
        summary = "add new role to user",
        description = """
            send header request to auth service with current admin token,
            set need roles and login user
                """,
        responses = {
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PutMapping("/user-roles/save")
    public ResponseEntity<?> saveNewRole(
        @RequestBody UserRolesDto userRolesDto,
        @RequestHeader("Authorization") String currentAdminToken) {
        String token = currentAdminToken.replaceFirst("^Bearer\\s+", "");
        return ResponseEntity.ok(authConnect.addRole(token, userRolesDto));
    }

    @Operation(
        summary = "cehck all rols by login user",
        responses = {
            @ApiResponse(responseCode = "201", description = "request is correctly"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "401", description = "not authorized user"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @GetMapping("/user-roles/{login}")
    public ResponseEntity<?> getMethodName(@PathVariable String login) {
        return ResponseEntity.ok(dealService.getAllUserRoles(login));
    }

}

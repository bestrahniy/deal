package com.deal.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.deal.Dto.AddNewRoleDto;
import com.deal.Dto.LogicalDeleteRoleDto;
import com.deal.services.ContractorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
    name = "manage to contractor to role endpoint"
)
@RestController
@RequestMapping("/contractor-to-role")
@AllArgsConstructor
public class ContractorToRoleController {

    private final ContractorService contractorService;

    @Operation(
        summary = "add new contractor role",
        description = "add new role to deal contractor",
        responses = {
            @ApiResponse(responseCode = "200", description = "new role added"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @PostMapping("/add")
    public ResponseEntity<?> addNewContractorRole(@RequestBody AddNewRoleDto addNewRoleDto) {
        contractorService.addNewRole(addNewRoleDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "logical delete role",
        description = "variable is_active becomes to false",
        responses = {
            @ApiResponse(responseCode = "204", description = "logical delete is successfull"),
            @ApiResponse(responseCode = "400", description = "incorrect data"),
            @ApiResponse(responseCode = "404", description = "url not found"),
            @ApiResponse(responseCode = "500", description = "server is badly")
        }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRole(@RequestBody LogicalDeleteRoleDto dto) {
        contractorService.deleteRole(dto);
        return ResponseEntity.noContent().build();
    }

}

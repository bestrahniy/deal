package com.deal.models;

import java.io.Serializable;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ContractorToRoleId implements Serializable {

    @Column(name = "contractor_id", columnDefinition = "UUID", nullable = false)
    private UUID contractorId;

    @Column(name = "role_id", columnDefinition = "VARCHAR(30)", nullable = false, length = 30)
    private String roleId;

}

package com.deal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "contractor_to_role")
public class ContractorToRole {

    @EmbeddedId
    private ContractorToRoleId contractorToRoleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id")
    @MapsId("contractorId")
    @JsonIgnore
    private DealContractor dealContractor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @MapsId("roleId")
    @JsonIgnore
    private ContractorRole contractorRole;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

}

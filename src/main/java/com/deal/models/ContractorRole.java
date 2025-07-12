package com.deal.models;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "contractor_role")
@AllArgsConstructor
@NoArgsConstructor
public class ContractorRole {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(30)", nullable = false, length = 30)
    private String id;

    @Column(name = "name", columnDefinition = "text")
    private String name;

    @Column(name = "category", columnDefinition = "VARCHAR(30)", nullable = false, length = 30)
    private String category;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "role_id")
    private List<ContractorToRole> contractorToRoles;

}

package com.deal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Entity
@Table(name = "deal_contractor")
public class DealContractor {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false)
    private Deal deal;

    @Column(name = "contractor_id", length = 12, nullable = false)
    private String contractorId;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @Column(name = "inn", columnDefinition = "text")
    private String inn;

    @Column(name = "main", nullable = false)
    private boolean main = false;

    @Column(name = "create_date", nullable = false, columnDefinition = "timestamp")
    @CreatedDate
    private Instant createDate = Instant.now();

    @Column(name = "modify_date", columnDefinition = "timestamp")
    @LastModifiedDate
    private Instant modifyDate;

    @Column(name = "create_user_id", columnDefinition = "text")
    @CreatedBy
    private String createUserId;

    @Column(name = "modify_user_id", columnDefinition = "text")
    @LastModifiedBy
    private String modifyUserId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contractor_id")
    private List<ContractorToRole> contractor_to_role;
}

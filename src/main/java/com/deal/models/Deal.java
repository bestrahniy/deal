package com.deal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Entity
@Table(name = "deal")
public class Deal {
    
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
    @GeneratedValue
    private UUID id;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "agreement_number", columnDefinition = "text")
    private String agreementNumber;

    @Column(name = "agreement_date", columnDefinition = "date")
    private LocalDate agreementDate;

    @Column(name = "agreement_start_dt", columnDefinition = "timestamp")
    private Instant agreementStartDt;

    @Column(name = "availability_date", columnDefinition = "date")
    private LocalDate availabilityDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private DealType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private DealStatus status;

    @Column(name = "close_dt", columnDefinition = "timestamp")
    private Instant closeDt;

    @CreatedDate
    @Column(name = "create_date", columnDefinition = "timestamp", nullable = false)
    private Instant createDate = Instant.now();

    @LastModifiedDate
    @Column(name = "modify_date", columnDefinition = "timestamp")
    private Instant modifyDate;

    @CreatedBy
    @Column(name = "create_user_id", columnDefinition = "text")
    private String createUserId;

    @LastModifiedBy
    @Column(name = "modify_user_id", columnDefinition = "text")
    private String modifyUserId;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "deal_id")
    private List<DealSum> sums;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "deal_id")
    private List<DealContractor> contractors;
}

package com.deal.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "deal")
public class Deal {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
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

    // связь на справочник статусов
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private DealStatus status;

    @Column(name = "close_dt", columnDefinition = "timestamp")
    private Instant closeDt;

    @Column(name = "create_date", nullable = false, columnDefinition = "timestamp")
    private Instant createDate = Instant.now();

    @Column(name = "modify_date", columnDefinition = "timestamp")
    private Instant modifyDate;

    @Column(name = "create_user_id", columnDefinition = "text")
    private String createUserId;

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

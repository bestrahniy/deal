package com.deal.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "deal_contractor")
@AllArgsConstructor
@NoArgsConstructor
public class DealContractor {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false)
    @JsonIgnore
    private Deal deal;

    @Column(name = "contractor_id", length = 12)
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
    private List<ContractorToRole> contractorToRole;

}

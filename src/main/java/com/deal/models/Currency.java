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
import lombok.Data;

@Data
@Entity
@Table(name = "Currency")
public class Currency {
    
    @Id
    @Column(length = 3, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean is_active = true;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "currency_id")
    private List<DealSum> dealsums;
}

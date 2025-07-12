package com.deal.Dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchDealFilterDto {

    private UUID dealId;

    private String description;

    private String agreementNumber;

    private LocalDate agreementDateFrom;

    private LocalDate agreementDateTo;

    private LocalDate availabilityDateFrom;

    private LocalDate availabilityDateTo;

    private List<String> dealTypes;

    private List<String> status;

    private LocalDateTime closeDtFrom;

    private LocalDateTime closeDtTo;

    private String borrowerSearch;

    private String warranitySearch;

    private BigDecimal sumValue;

    private String sumCurrency;

    private Pageable pageable;

}

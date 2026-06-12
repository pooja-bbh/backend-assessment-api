package com.backend.assessment.infrastructure.persistence.entity;

import com.backend.assessment.domain.models.LineOfBusiness;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.Region;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policies")
public class PolicyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    private Region region;

    @Enumerated(EnumType.STRING)
    @Column(name = "line_of_business", nullable = false)
    private LineOfBusiness lineOfBusiness;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PolicyStatus status;

    @Column(name = "premium_amount", nullable = false)
    private BigDecimal premiumAmount;

    @Column(name = "premium_currency", nullable = false)
    private String premiumCurrency;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "flagged_for_review", nullable = false)
    private boolean flaggedForReview;

    protected PolicyEntity() {
    }

    public PolicyEntity(
            String policyNumber,
            String holderName,
            Region region,
            LineOfBusiness lineOfBusiness,
            PolicyStatus status,
            BigDecimal premiumAmount,
            String premiumCurrency,
            LocalDate startDate,
            LocalDate endDate) {
        this.policyNumber = policyNumber;
        this.holderName = holderName;
        this.region = region;
        this.lineOfBusiness = lineOfBusiness;
        this.status = status;
        this.premiumAmount = premiumAmount;
        this.premiumCurrency = premiumCurrency;
        this.startDate = startDate;
        this.endDate = endDate;
        this.flaggedForReview = false;
    }

    public Long getId() {
        return id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public Region getRegion() {
        return region;
    }

    public LineOfBusiness getLineOfBusiness() {
        return lineOfBusiness;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public String getPremiumCurrency() {
        return premiumCurrency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isFlaggedForReview() {
        return flaggedForReview;
    }
}

package com.backend.assessment.infrastructure.persistence.repository;

import com.backend.assessment.domain.models.LineOfBusiness;
import com.backend.assessment.domain.models.PolicyStatus;
import com.backend.assessment.domain.models.Region;
import com.backend.assessment.infrastructure.persistence.entity.PolicyEntity;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {

    @Query("select p from PolicyEntity p where "
            + "(:status is null or p.status = :status) and "
            + "(:lineOfBusiness is null or p.lineOfBusiness = :lineOfBusiness) and "
            + "(:region is null or p.region = :region) and "
            + "(:endDateFrom is null or p.endDate >= :endDateFrom) and "
            + "(:endDateTo is null or p.endDate <= :endDateTo) and "
            + "(:searchTerm is null "
            + "or lower(p.policyNumber) like lower(concat('%', cast(:searchTerm as string), '%')) "
            + "or lower(p.holderName) like lower(concat('%', cast(:searchTerm as string), '%')))")
    Page<PolicyEntity> search(
            @Param("status") PolicyStatus status,
            @Param("lineOfBusiness") LineOfBusiness lineOfBusiness,
            @Param("region") Region region,
            @Param("endDateFrom") LocalDate endDateFrom,
            @Param("endDateTo") LocalDate endDateTo,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);

    @Modifying
    @Query("update PolicyEntity p set p.flaggedForReview = true where p.id in :ids")
    int flagForReview(@Param("ids") Collection<Long> ids);

    @Query("select p.id from PolicyEntity p where p.id in :ids")
    List<Long> findExistingIds(@Param("ids") Collection<Long> ids);

    @Query("select p.status as status, count(p) as count from PolicyEntity p group by p.status")
    List<StatusCount> countGroupedByStatus();

    @Query("select p.lineOfBusiness as lineOfBusiness, sum(p.premiumAmount) as totalPremium "
            + "from PolicyEntity p group by p.lineOfBusiness")
    List<LineOfBusinessPremium> sumPremiumGroupedByLineOfBusiness();

    long countByEndDateBetween(LocalDate startInclusive, LocalDate endInclusive);
}

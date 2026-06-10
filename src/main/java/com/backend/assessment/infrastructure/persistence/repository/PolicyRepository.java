package com.backend.assessment.infrastructure.persistence.repository;

import com.backend.assessment.infrastructure.persistence.entity.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {
}

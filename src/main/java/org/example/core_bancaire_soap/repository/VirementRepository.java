package org.example.core_bancaire_soap.repository;

import org.example.core_bancaire_soap.entity.Virement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirementRepository extends JpaRepository<Virement, Integer> {
}

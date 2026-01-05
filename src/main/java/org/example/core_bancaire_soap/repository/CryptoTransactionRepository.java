package org.example.core_bancaire_soap.repository;

import org.example.core_bancaire_soap.entity.CryptoTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoTransactionRepository extends JpaRepository<CryptoTransaction, Integer> {
}
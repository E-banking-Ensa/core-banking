package org.example.core_bancaire_soap.repository;

import org.example.core_bancaire_soap.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}

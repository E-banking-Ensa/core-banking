package org.example.core_bancaire_soap.repository;

import org.example.core_bancaire_soap.entity.CryptoWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptoWalletRepository extends JpaRepository<CryptoWallet,Long> {
}

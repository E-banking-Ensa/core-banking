package org.example.core_bancaire_soap.repository;

import org.example.core_bancaire_soap.entity.CryptoBalance;
import org.example.core_bancaire_soap.entity.CryptoWallet;
import org.example.core_bancaire_soap.entity.CryptoCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CryptoBalanceRepository extends JpaRepository<CryptoBalance, Long> {
    Optional<CryptoBalance> findByWalletAndCurrency(CryptoWallet wallet, CryptoCurrency currency);
}
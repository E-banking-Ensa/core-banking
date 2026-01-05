package org.example.core_bancaire_soap.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@PrimaryKeyJoinColumn(name = "id") // Hérite de la table 'Transaction'
public class CryptoTransaction extends Transaction {

    // --- Champs spécifiques Crypto ---

    @Enumerated(EnumType.STRING)
    private CryptoCurrency cryptoCurrency; // Enum (BTC, ETH...)

    private Double cryptoAmount; // Montant en crypto (ex: 0.0045)

    private Double exchangeRate; // Taux utilisé au moment de la transaction

    // --- Champs que vous essayez d'utiliser (qui manquaient) ---

    private Double fiatAmount; // Montant en devise locale (ex: 20000 MAD)

    @Enumerated(EnumType.STRING)
    private AccountType sourceType; // Enum (BANK_ACCOUNT, CRYPTO_WALLET)

    private Long sourceRef; // ID du compte source

    @Enumerated(EnumType.STRING)
    private AccountType destinationType; // Enum (BANK_ACCOUNT, CRYPTO_WALLET)

    private Long destinationRef; // ID du compte destination

    // Type de transaction crypto (BUY, SELL) si nécessaire
    @Enumerated(EnumType.STRING)
    private CryptoTransactionType cryptoType;
}
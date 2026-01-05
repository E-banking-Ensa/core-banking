package org.example.core_bancaire_soap.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "crypto_balances")
public class CryptoBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    private CryptoWallet wallet;

    @Enumerated(EnumType.STRING)
    private CryptoCurrency currency; // Enum: BTC, ETH

    private Double amount; // Le solde

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CryptoWallet getWallet() {
        return wallet;
    }

    public void setWallet(CryptoWallet wallet) {
        this.wallet = wallet;
    }

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
package org.example.core_bancaire_soap.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "crypto_wallets")

public class CryptoWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Un wallet par user
    private Long userId;

    // Relation vers les balances (BTC, ETH, etc.)
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<CryptoBalance> balances;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CryptoBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<CryptoBalance> balances) {
        this.balances = balances;
    }
}
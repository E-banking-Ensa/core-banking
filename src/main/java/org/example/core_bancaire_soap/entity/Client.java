package org.example.core_bancaire_soap.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Client extends User {
    private String address;
    private String identificationNumber;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Account> accounts;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "crypto_wallet_id", referencedColumnName = "id")
    private CryptoWallet cryptoWallet;


    public CryptoWallet getCryptoWallet() { return cryptoWallet; }
    public void setCryptoWallet(CryptoWallet cryptoWallet) { this.cryptoWallet = cryptoWallet; }

}
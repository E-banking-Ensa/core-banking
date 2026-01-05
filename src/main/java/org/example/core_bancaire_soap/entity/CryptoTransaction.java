package org.example.core_bancaire_soap.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@PrimaryKeyJoinColumn(name = "id") // Join avec la table parent Transaction
public class CryptoTransaction extends Transaction {

    @ManyToOne
    private CryptoWallet cryptoWallet; // Le wallet impliqué

    private double cryptoAmount; // Le montant en devise crypto (ex: 0.0045 BTC)

    private double exchangeRate; // Le taux appliqué au moment de la transaction

    private String cryptoCurrency; // La devise (BTC, ETH, etc.)
}
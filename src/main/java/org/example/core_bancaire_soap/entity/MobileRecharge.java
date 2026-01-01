package org.example.core_bancaire_soap.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("RECHARGE")
public class MobileRecharge extends Transaction {
    @ManyToOne
    private Account account;
    private String phoneNumber;

}
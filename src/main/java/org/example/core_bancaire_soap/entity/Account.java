package org.example.core_bancaire_soap.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "accounts")

public class Account {
    @Id
    private int id;
    private Double balance;
    private String currency;
    @ManyToOne(cascade = CascadeType.ALL)
    private Client client;
    @OneToMany
    private List<Account> beneficiere;
    @OneToMany
    private List<Transaction> transactions;
    private double rib;
    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Account> getBeneficiere() {
        return beneficiere;
    }

    public void setBeneficiere(List<Account> beneficiere) {
        this.beneficiere = beneficiere;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public double getRib() {
        return rib;
    }

    public void setRib(double rib) {
        this.rib = rib;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public User getUser() {
        return client;
    }

    public void setUser(Client client) {
        this.client = client;
    }
}
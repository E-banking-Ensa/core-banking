package org.example.core_bancaire_soap.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public  class Transaction {
    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private Double Montant;
    private LocalDateTime date;
    @Enumerated(EnumType.STRING)
    private TypeTransaction type;









}
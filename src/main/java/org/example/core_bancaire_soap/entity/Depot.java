package org.example.core_bancaire_soap.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Depot extends Transaction{
    @ManyToOne
    private Account account;
}

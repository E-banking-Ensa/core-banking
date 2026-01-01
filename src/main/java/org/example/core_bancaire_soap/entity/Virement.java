package org.example.core_bancaire_soap.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Virement extends Transaction {
    @ManyToOne
    private Account Source;
    @ManyToOne
    private Account Destination;
    private String Motife;
    @Enumerated(EnumType.STRING)
    private VirementType type_virement;


}
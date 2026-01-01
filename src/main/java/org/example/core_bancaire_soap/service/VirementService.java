package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.core_bancaire_soap.entity.Account;
import org.example.core_bancaire_soap.entity.TypeTransaction;
import org.example.core_bancaire_soap.entity.Virement;
import org.example.core_bancaire_soap.repository.AccountRepository;
import org.example.core_bancaire_soap.repository.VirementRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VirementService {
    private final AccountRepository accountRepository;
    private final VirementRepository virementRepository;

    @Transactional // <--- CRITIQUE : Tout ou rien
    public void virement(int sourceId, int destId, double montant,String motif) {

        // 1. Récupération des comptes
        List<Account> comptesConcernes = new ArrayList<>();
        Account source = accountRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Compte source introuvable : " + sourceId));

        Account destination = accountRepository.findById(destId)
                .orElseThrow(() -> new RuntimeException("Compte destination introuvable : " + destId));

        if (source.getBalance() < montant) {
            throw new RuntimeException("Solde insuffisant sur le compte " + sourceId);
        }

        source.setBalance(source.getBalance() - montant);
        destination.setBalance(destination.getBalance() + montant);
        accountRepository.save(source);
        accountRepository.save(destination);

        Virement virement = new Virement();
        virement.setSource(source);
        virement.setDestination(destination);
        virement.setMontant(montant);
        virement.setType(TypeTransaction.VIREMENT);
        virement.setDate(LocalDateTime.now());
        virement.setMotife(motif); // Exemple par défaut
        comptesConcernes.add(source);
        comptesConcernes.add(destination);
        virement.setAccounts(comptesConcernes);
        virementRepository.save(virement);


    }
}

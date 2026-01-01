package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.core_bancaire_soap.entity.Account;
import org.example.core_bancaire_soap.entity.Virement;
import org.example.core_bancaire_soap.repository.AccountRepository;
import org.example.core_bancaire_soap.repository.VirementRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VirementService {
    private final AccountRepository accountRepository;
    private final VirementRepository virementRepository;

    @Transactional // <--- CRITIQUE : Tout ou rien
    public void virement(int sourceId, int destId, double montant) {

        // 1. Récupération des comptes
        Account source = accountRepository.findById(sourceId)
                .orElseThrow(() -> new RuntimeException("Compte source introuvable : " + sourceId));

        Account destination = accountRepository.findById(destId)
                .orElseThrow(() -> new RuntimeException("Compte destination introuvable : " + destId));

        // 2. Vérification du solde (Règle métier)
        if (source.getBalance() < montant) {
            throw new RuntimeException("Solde insuffisant sur le compte " + sourceId);
        }

        // 3. Mise à jour des soldes (En mémoire)
        source.setBalance(source.getBalance() - montant);
        destination.setBalance(destination.getBalance() + montant);

        // 4. Sauvegarde des comptes modifiés
        accountRepository.save(source);
        accountRepository.save(destination);

        // 5. Enregistrement de l'historique (Trace d'audit)
        Virement virement = new Virement();
        virement.setSource(source);
        virement.setDestination(destination);
        // On suppose que ces setters existent dans la classe mère Transaction
        // virement.setAmount(montant);
        // virement.setDate(new Date());
        virement.setMotife("Virement SOAP"); // Exemple par défaut

        virementRepository.save(virement);
    }
}

package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.polytech.ebanking_soap.RechargeRequest;
import me.polytech.ebanking_soap.RechargeResponse;
import org.example.core_bancaire_soap.entity.Account;
import org.example.core_bancaire_soap.entity.MobileRecharge;
import org.example.core_bancaire_soap.entity.TypeTransaction;
import org.example.core_bancaire_soap.repository.AccountRepository;
import org.example.core_bancaire_soap.repository.RechargeRepo;
import org.example.core_bancaire_soap.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RechargeService {
    private final RechargeRepo rechargeRepo;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    @Transactional
    public RechargeResponse recharge(RechargeRequest request) {
        System.out.println("compte >>"+ request.getAccount());
        Account account = accountRepository.findById(request.getAccount())
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        if (account.getBalance() < request.getMontant()) {
            throw new RuntimeException("Solde insuffisant");
        }

        account.setBalance(account.getBalance() - request.getMontant());
        accountRepository.save(account);

        MobileRecharge recharge = new MobileRecharge();
        recharge.setMontant(request.getMontant());
        recharge.setPhoneNumber(request.getPhoneNumber());
        recharge.setDate(LocalDateTime.now());
        recharge.setType(TypeTransaction.RECHARGE);
        recharge.setAccount(account);
        recharge.setAccounts(List.of(account));

        // Sauvegarder via votre transactionRepository
        transactionRepository.save(recharge);

        RechargeResponse response = new RechargeResponse();
        response.setStatus("SUCCES");
        response.setTransactionId(recharge.getId());
        return response;
    }

}

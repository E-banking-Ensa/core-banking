package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.polytech.ebanking_soap.DepotRequest;
import me.polytech.ebanking_soap.DepotResponse;
import me.polytech.ebanking_soap.RetraitRequest;
import me.polytech.ebanking_soap.RetraitResponse;
import org.example.core_bancaire_soap.entity.Account;
import org.example.core_bancaire_soap.entity.Depot;
import org.example.core_bancaire_soap.entity.Transaction;
import org.example.core_bancaire_soap.entity.TypeTransaction;
import org.example.core_bancaire_soap.repository.AccountRepository;
import org.example.core_bancaire_soap.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DepotResponse depot(DepotRequest depotRequest)  {
        DepotResponse depotResponse = new DepotResponse();
        if( depotRequest != null) {
            if( accountRepository.findById(depotRequest.getAccount()).isPresent()) {
                Account account = accountRepository.findById(depotRequest.getAccount()).get();
                account.setBalance(account.getBalance() + depotRequest.getMontant());
                Depot d = new Depot();
                d.setMontant(depotRequest.getMontant());
                d.setDate(LocalDateTime.now());
                d.setType(TypeTransaction.DEPOSIT);
                d.setAccount(account);

                transactionRepository.save(d);
                accountRepository.save(account);
                depotResponse.setStatus("Success");
                depotResponse.setMontant(depotRequest.getMontant());
                return depotResponse;
            }
            else {
               depotResponse.setStatus("Not found");
               depotResponse.setMontant(depotRequest.getMontant());
               return depotResponse;
            }
        }else {
            depotResponse.setStatus("Request Empty");
            depotResponse.setMontant(depotRequest.getMontant());
            return depotResponse;

        }

    }

    public RetraitResponse retrait(RetraitRequest request) {
        RetraitResponse retraitResponse  = new RetraitResponse();
        if( request != null) {
            if( accountRepository.findById(request.getAccount()).isPresent()) {
                Account account = accountRepository.findById(request.getAccount()).get();
                if(account.getBalance() >= request.getMontant()) {
                account.setBalance(account.getBalance() - request.getMontant());
                }else {
                    retraitResponse.setStatus("solde not enough");
                    retraitResponse.setMontant(request.getMontant());
                    return retraitResponse;
                }
                accountRepository.save(account);
                retraitResponse.setStatus("Success");
                retraitResponse.setMontant(request.getMontant());
                return retraitResponse;
            }
            else {
                retraitResponse.setStatus("Not found");
                retraitResponse.setMontant(request.getMontant());
                return retraitResponse;
            }
        }else {
            retraitResponse.setStatus("Request Empty");
            retraitResponse.setMontant(request.getMontant());
            return retraitResponse;

        }
    }
}

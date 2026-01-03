package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.polytech.ebanking_soap.DepotRequest;
import me.polytech.ebanking_soap.DepotResponse;
import me.polytech.ebanking_soap.RetraitRequest;
import me.polytech.ebanking_soap.RetraitResponse;
import org.example.core_bancaire_soap.entity.*;
import org.example.core_bancaire_soap.repository.AccountRepository;
import org.example.core_bancaire_soap.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
                d.setAccounts(List.of(account));

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
                Retrait r = new Retrait();
                r.setAccount(account);
                r.setMontant(account.getBalance());
                r.setDate(LocalDateTime.now());
                r.setType(TypeTransaction.RETRAIT);
                r.setAccounts(List.of(account));
                transactionRepository.save(r);

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

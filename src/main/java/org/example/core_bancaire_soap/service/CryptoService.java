package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.core_bancaire_soap.entity.*;
import org.example.core_bancaire_soap.repository.AccountRepository;
import org.example.core_bancaire_soap.repository.CryptoTransactionRepository;
import org.example.core_bancaire_soap.repository.CryptoWalletRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private final AccountRepository accountRepository;
    private final CryptoWalletRepository cryptoWalletRepository;
    private final CryptoTransactionRepository cryptoTransactionRepository;


    @Transactional
    public CryptoTransaction buyCrypto(int accountId, Long walletId, double fiatAmount, String cryptoCurrency, double exchangeRate) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        CryptoWallet wallet = cryptoWalletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet introuvable"));

        if (account.getBalance() < fiatAmount) {
            throw new RuntimeException("Solde insuffisant");
        }


        double cryptoAmount = fiatAmount / exchangeRate;

        account.setBalance(account.getBalance() - fiatAmount);

        if (wallet.getCurrency() == null) wallet.setCurrency(cryptoCurrency);
        wallet.setBalance((wallet.getBalance() != null ? wallet.getBalance() : 0.0) + cryptoAmount);

        accountRepository.save(account);
        cryptoWalletRepository.save(wallet);

        // 4. Transaction
        CryptoTransaction tx = new CryptoTransaction();
        tx.setMontant(fiatAmount);
        tx.setDate(LocalDateTime.now());
        tx.setType(TypeTransaction.CRYPTO_BUY);
        tx.setAccounts(Collections.singletonList(account));
        tx.setCryptoWallet(wallet);
        tx.setCryptoAmount(cryptoAmount);
        tx.setExchangeRate(exchangeRate);
        tx.setCryptoCurrency(cryptoCurrency);

        return cryptoTransactionRepository.save(tx);
    }

    @Transactional
    public CryptoTransaction sellCrypto(int accountId, Long walletId, double cryptoAmount, double exchangeRate) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));
        CryptoWallet wallet = cryptoWalletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet introuvable"));

        if (wallet.getBalance() == null || wallet.getBalance() < cryptoAmount) {
            throw new RuntimeException("Solde crypto insuffisant");
        }

        double fiatAmount = cryptoAmount * exchangeRate;

        wallet.setBalance(wallet.getBalance() - cryptoAmount);
        account.setBalance(account.getBalance() + fiatAmount);

        accountRepository.save(account);
        cryptoWalletRepository.save(wallet);

        CryptoTransaction tx = new CryptoTransaction();
        tx.setMontant(fiatAmount);
        tx.setDate(LocalDateTime.now());
        tx.setType(TypeTransaction.CRYPTO_SELL);
        tx.setAccounts(Collections.singletonList(account));
        tx.setCryptoWallet(wallet);
        tx.setCryptoAmount(cryptoAmount);
        tx.setExchangeRate(exchangeRate);
        tx.setCryptoCurrency(wallet.getCurrency());

        return cryptoTransactionRepository.save(tx);
    }
}
package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.core_bancaire_soap.entity.*;
import org.example.core_bancaire_soap.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private final AccountRepository accountRepository;
    private final CryptoWalletRepository cryptoWalletRepository;
    private final CryptoTransactionRepository cryptoTransactionRepository;
    // Indispensable pour gérer les sous-comptes (BTC, ETH) d'un wallet
    private final CryptoBalanceRepository cryptoBalanceRepository;

    /**
     * ACHAT DE CRYPTO (Banque -> Crypto)
     */
    @Transactional
    public CryptoTransaction buyCrypto(int accountId, Long walletId, double fiatAmount, String currencyStr, double exchangeRate) {

        // 1. Récupération des entités
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte bancaire introuvable"));

        CryptoWallet wallet = cryptoWalletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet introuvable"));

        CryptoCurrency currency = CryptoCurrency.valueOf(currencyStr);

        // 2. Vérification et Débit Bancaire (Fiat)
        if (account.getBalance() < fiatAmount) {
            throw new RuntimeException("Solde bancaire insuffisant");
        }
        account.setBalance(account.getBalance() - fiatAmount);
        accountRepository.save(account);

        // 3. Crédit Crypto (Via CryptoBalance)
        // On cherche le sous-compte de la devise (ex: BTC), sinon on le crée
        CryptoBalance balance = cryptoBalanceRepository.findByWalletAndCurrency(wallet, currency)
                .orElseGet(() -> {
                    CryptoBalance newBalance = new CryptoBalance();
                    newBalance.setWallet(wallet);
                    newBalance.setCurrency(currency);
                    newBalance.setAmount(0.0);
                    return cryptoBalanceRepository.save(newBalance);
                });

        // Calcul du montant crypto reçu (Fiat / Taux)
        double cryptoAmount = fiatAmount / exchangeRate;
        balance.setAmount(balance.getAmount() + cryptoAmount);
        cryptoBalanceRepository.save(balance);

        // 4. Création et Sauvegarde de la Transaction
        CryptoTransaction tx = new CryptoTransaction();

        // --- Champs hérités de Transaction (Core Banking) ---
        tx.setMontant(fiatAmount); // On stocke la valeur en devise locale
        tx.setDate(LocalDateTime.now());
        tx.setType(TypeTransaction.CRYPTO_BUY); // Enum global
        tx.setAccounts(Collections.singletonList(account)); // Lien JPA

        // --- Champs spécifiques Crypto ---
        tx.setCryptoCurrency(currency);
        tx.setCryptoAmount(cryptoAmount);
        tx.setExchangeRate(exchangeRate);
        tx.setFiatAmount(fiatAmount);

        // Traçabilité
        tx.setSourceType(AccountType.BANK_ACCOUNT);
        tx.setSourceRef((long) account.getId());
        tx.setDestinationType(AccountType.CRYPTO_WALLET);
        tx.setDestinationRef(wallet.getId());

        tx.setCryptoType(CryptoTransactionType.BUY);

        // IMPORTANT : On retourne l'objet pour que le Endpoint puisse lire l'ID
        return cryptoTransactionRepository.save(tx);
    }

    /**
     * VENTE DE CRYPTO (Crypto -> Banque)
     */
    @Transactional
    public CryptoTransaction sellCrypto(int accountId, Long walletId, double cryptoAmount, double exchangeRate) {

        // 1. Récupération des entités
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte bancaire introuvable"));

        CryptoWallet wallet = cryptoWalletRepository.findById(walletId)
                .orElseThrow(() -> new RuntimeException("Wallet introuvable"));

        // NOTE: Le XSD 'sellCryptoRequest' ne précise pas la devise.
        // On suppose ici BTC par défaut ou on prend la première balance disponible.
        // Pour cet exemple, on fixe BTC.
        CryptoCurrency currency = CryptoCurrency.BTC;

        // 2. Vérification et Débit Crypto
        CryptoBalance balance = cryptoBalanceRepository.findByWalletAndCurrency(wallet, currency)
                .orElseThrow(() -> new RuntimeException("Aucun portefeuille " + currency + " trouvé"));

        if (balance.getAmount() < cryptoAmount) {
            throw new RuntimeException("Solde crypto insuffisant");
        }

        balance.setAmount(balance.getAmount() - cryptoAmount);
        cryptoBalanceRepository.save(balance);

        // 3. Crédit Bancaire (Fiat)
        // Calcul du montant Fiat reçu (Crypto * Taux)
        double fiatAmount = cryptoAmount * exchangeRate;
        account.setBalance(account.getBalance() + fiatAmount);
        accountRepository.save(account);

        // 4. Création et Sauvegarde de la Transaction
        CryptoTransaction tx = new CryptoTransaction();

        // --- Champs hérités ---
        tx.setMontant(fiatAmount); // Gain en devise locale
        tx.setDate(LocalDateTime.now());
        tx.setType(TypeTransaction.CRYPTO_SELL);
        tx.setAccounts(Collections.singletonList(account));

        // --- Champs spécifiques ---
        tx.setCryptoCurrency(currency);
        tx.setCryptoAmount(cryptoAmount);
        tx.setExchangeRate(exchangeRate);
        tx.setFiatAmount(fiatAmount);

        // Traçabilité (Inverse de l'achat)
        tx.setSourceType(AccountType.CRYPTO_WALLET);
        tx.setSourceRef(wallet.getId());
        tx.setDestinationType(AccountType.BANK_ACCOUNT);
        tx.setDestinationRef((long) account.getId());

        tx.setCryptoType(CryptoTransactionType.SELL);

        // IMPORTANT : On retourne l'objet
        return cryptoTransactionRepository.save(tx);
    }
}
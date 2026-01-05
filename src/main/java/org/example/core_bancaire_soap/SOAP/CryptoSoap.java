package org.example.core_bancaire_soap.SOAP;

import lombok.RequiredArgsConstructor;
import org.example.core_bancaire_soap.service.CryptoService;

// Attention : Importez bien la classe CryptoTransaction de VOTRE modèle unifié
// (Soit org.bankati.cryptoservice.model si vous avez fusionné, soit org.example.core_bancaire_soap.entity)
import org.example.core_bancaire_soap.entity.CryptoTransaction;

// Imports JAXB générés
import me.polytech.ebanking_soap.BuyCryptoRequest;
import me.polytech.ebanking_soap.BuyCryptoResponse;
import me.polytech.ebanking_soap.SellCryptoRequest;
import me.polytech.ebanking_soap.SellCryptoResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class CryptoSoap {

    private static final String NAMESPACE_URI = "http://www.polytech.me/ebanking-soap"; // Vérifiez votre XSD

    private final CryptoService cryptoService;

    // --- ACHAT ---
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "buyCryptoRequest")
    @ResponsePayload
    public BuyCryptoResponse buyCrypto(@RequestPayload BuyCryptoRequest request) {
        BuyCryptoResponse response = new BuyCryptoResponse();
        try {
            // ICI : On récupère le exchangeRate de la requête
            CryptoTransaction tx = cryptoService.buyCrypto(
                    request.getAccountId(),
                    request.getCryptoWalletId(),
                    request.getFiatAmount(),
                    request.getCryptoCurrency(),
                    request.getExchangeRate() // <--- CRUCIAL : On passe le taux reçu
            );

            response.setStatus("SUCCESS");
            // On renvoie les infos de la transaction créée
            response.setTransactionId(tx.getId());
            response.setRateUsed(tx.getExchangeRate());
            response.setCryptoAmountReceived(tx.getCryptoAmount()); // ou tx.getAmount() selon votre entité

        } catch (Exception e) {
            response.setStatus("FAILED: " + e.getMessage());
            // Valeurs par défaut en cas d'erreur
            response.setTransactionId(0);
            response.setRateUsed(0.0);
            response.setCryptoAmountReceived(0.0);
        }
        return response;
    }

    // --- VENTE ---
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "sellCryptoRequest")
    @ResponsePayload
    public SellCryptoResponse sellCrypto(@RequestPayload SellCryptoRequest request) {
        SellCryptoResponse response = new SellCryptoResponse();
        try {
            // ICI : On récupère le exchangeRate
            CryptoTransaction tx = cryptoService.sellCrypto(
                    request.getAccountId(),
                    request.getCryptoWalletId(),
                    request.getCryptoAmount(),
                    request.getExchangeRate() // <--- CRUCIAL
            );

            response.setStatus("SUCCESS");
            response.setTransactionId(tx.getId());
            response.setRateUsed(tx.getExchangeRate());
            response.setFiatAmountReceived(tx.getFiatAmount()); // ou tx.getMontant()

        } catch (Exception e) {
            response.setStatus("FAILED: " + e.getMessage());
            response.setTransactionId(0);
            response.setRateUsed(0.0);
            response.setFiatAmountReceived(0.0);
        }
        return response;
    }
}
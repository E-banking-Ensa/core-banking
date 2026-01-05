package org.example.core_bancaire_soap.SOAP;

import lombok.RequiredArgsConstructor;
import org.example.core_bancaire_soap.entity.CryptoTransaction;
import org.example.core_bancaire_soap.service.CryptoService;

// Ces imports dépendent de la génération JAXB (vérifiez votre package cible)
// Par défaut avec votre namespace : me.polytech.ebanking_soap
import me.polytech.ebanking_soap.BuyCryptoRequest;
import me.polytech.ebanking_soap.BuyCryptoResponse;
import me.polytech.ebanking_soap.SellCryptoRequest;
import me.polytech.ebanking_soap.SellCryptoResponse;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor // Injection par constructeur comme dans votre Service
public class CryptoSoap {

    private static final String NAMESPACE_URI = "http://www.polytech.me/ebanking-soap";

    private final CryptoService cryptoService;

    // Dans CryptoSoapEndpoint.java

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "buyCryptoRequest")
    @ResponsePayload
    public BuyCryptoResponse buyCrypto(@RequestPayload BuyCryptoRequest request) {
        BuyCryptoResponse response = new BuyCryptoResponse();
        try {
            // On passe request.getExchangeRate() au service
            CryptoTransaction tx = cryptoService.buyCrypto(
                    request.getAccountId(),
                    request.getCryptoWalletId(),
                    request.getFiatAmount(),
                    request.getCryptoCurrency(),
                    request.getExchangeRate() // <-- Nouveau paramètre
            );

            response.setStatus("SUCCESS");
            response.setTransactionId(tx.getId());
            response.setRateUsed(tx.getExchangeRate());
            response.setCryptoAmountReceived(tx.getCryptoAmount());

        } catch (Exception e) {
            response.setStatus("FAILED: " + e.getMessage());
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "sellCryptoRequest")
    @ResponsePayload
    public SellCryptoResponse sellCrypto(@RequestPayload SellCryptoRequest request) {
        SellCryptoResponse response = new SellCryptoResponse();
        try {
            // On passe request.getExchangeRate() au service
            CryptoTransaction tx = cryptoService.sellCrypto(
                    request.getAccountId(),
                    request.getCryptoWalletId(),
                    request.getCryptoAmount(),
                    request.getExchangeRate() // <-- Nouveau paramètre
            );

            response.setStatus("SUCCESS");
            response.setTransactionId(tx.getId());
            response.setRateUsed(tx.getExchangeRate());
            response.setFiatAmountReceived(tx.getMontant());

        } catch (Exception e) {
            response.setStatus("FAILED: " + e.getMessage());
        }
        return response;
    }
}
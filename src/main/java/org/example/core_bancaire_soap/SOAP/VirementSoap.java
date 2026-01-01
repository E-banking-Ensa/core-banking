package org.example.core_bancaire_soap.SOAP;

import lombok.RequiredArgsConstructor;
import me.polytech.ebanking_soap.VirementRequest;
import me.polytech.ebanking_soap.VirementResponse;
import org.example.core_bancaire_soap.service.AccountService;
import org.example.core_bancaire_soap.service.VirementService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
@RequiredArgsConstructor
public class VirementSoap {
    private static final String NAMESPACE = "http://www.polytech.me/ebanking-soap";
    private final VirementService virementService;
    @PayloadRoot(namespace = NAMESPACE, localPart = "VirementRequest")
    @ResponsePayload
    public VirementResponse virement(@RequestPayload VirementRequest request) {
        VirementResponse response = new VirementResponse();

        try {
            // Conversion BigInteger -> int pour vos IDs
            int sourceId = request.getSource().intValue();
            int destId = request.getDest().intValue();

            // Appel du service métier
            virementService.virement(sourceId, destId, request.getMontant());

            response.setStatus("SUCCES");
        } catch (RuntimeException e) {
            // En cas d'erreur (solde insuffisant, compte inexistant...)
            response.setStatus("ERREUR : " + e.getMessage());
        }

        return response;
    }
}

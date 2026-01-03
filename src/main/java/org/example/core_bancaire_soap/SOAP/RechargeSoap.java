package org.example.core_bancaire_soap.SOAP;

import lombok.RequiredArgsConstructor;
import me.polytech.ebanking_soap.RechargeRequest;
import me.polytech.ebanking_soap.RechargeResponse;
import org.example.core_bancaire_soap.service.RechargeService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@RequiredArgsConstructor
@Endpoint
public class RechargeSoap {
    private static final String NAMESPACE = "http://www.polytech.me/ebanking-soap";
    private final RechargeService rechargeService;
    @PayloadRoot(namespace = NAMESPACE, localPart = "rechargeRequest")
    @ResponsePayload
    public RechargeResponse recharge(@RequestPayload RechargeRequest request) {
        return rechargeService.recharge(request);
    }
}

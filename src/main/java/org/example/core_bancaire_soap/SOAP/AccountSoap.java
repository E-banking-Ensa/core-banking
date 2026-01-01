package org.example.core_bancaire_soap.SOAP;
import lombok.RequiredArgsConstructor;
import me.polytech.ebanking_soap.*;
import org.example.core_bancaire_soap.service.AccountService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@RequiredArgsConstructor
@Endpoint
public class AccountSoap {
    private static final String NAMESPACE = "http://www.polytech.me/ebanking-soap";

    private final AccountService accountService;

    @PayloadRoot(namespace = NAMESPACE, localPart = "depotRequest")
    @ResponsePayload
    public DepotResponse depot(@RequestPayload DepotRequest request)   {
        System.out.println(request.getAccount()+" test");
        return accountService.depot(request);

    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "RetraitRequest")
    @ResponsePayload
    public RetraitResponse retrait(@RequestPayload RetraitRequest request)   {
        return accountService.retrait(request);

    }

}
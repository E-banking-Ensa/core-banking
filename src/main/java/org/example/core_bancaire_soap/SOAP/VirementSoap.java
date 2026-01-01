package org.example.core_bancaire_soap.SOAP;

import me.polytech.ebanking_soap.VirementRequest;
import me.polytech.ebanking_soap.VirementResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class VirementSoap {
    private static final String NAMESPACE = "http://www.polytech.me/ebanking-soap";

//    @PayloadRoot(namespace = NAMESPACE, localPart = "virementRequest")
//    @ResponsePayload
//    public VirementResponse virementClassic(@RequestPayload VirementRequest request){
//
//    }
}

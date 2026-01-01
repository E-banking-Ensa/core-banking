package org.example.core_bancaire_soap.service;

import lombok.RequiredArgsConstructor;
import org.example.core_bancaire_soap.repository.VirementRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VirementService {
    private final VirementRepository virementRepository;


}

package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.core_bancaire_soap.client.UserServiceClient;
import org.example.core_bancaire_soap.client.dto.CreateUserRequest;
import org.example.core_bancaire_soap.client.dto.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service utilisateur qui délègue les opérations au user-service centralisé.
 * Ce service agit comme un adaptateur pour maintenir la compatibilité avec le code SOAP existant.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserServiceClient userServiceClient;

    /**
     * Crée un utilisateur via le user-service centralisé
     */
    public UserResponse createUser(String firstName, String lastName, String email) {
        log.info("Creating user via user-service: {} {} ({})", firstName, lastName, email);
        
        CreateUserRequest request = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .username(email.split("@")[0]) // Générer un username basé sur l'email
                .role("Client")
                .build();
        
        ResponseEntity<UserResponse> response = userServiceClient.syncUser(request);
        if (response.getBody() == null) {
            throw new RuntimeException("Failed to create user in user-service");
        }
        return response.getBody();
    }

    /**
     * Récupère un utilisateur par son UUID via le user-service centralisé
     */
    public UserResponse getUser(UUID userId) {
        log.info("Getting user from user-service: {}", userId);
        
        ResponseEntity<UserResponse> response = userServiceClient.getUser(userId);
        if (response.getBody() == null) {
            throw new RuntimeException("Utilisateur non trouvé: " + userId);
        }
        return response.getBody();
    }

    /**
     * Récupère un utilisateur par son email via le user-service centralisé
     */
    public UserResponse getUserByEmail(String email) {
        log.info("Getting user by email from user-service: {}", email);
        
        ResponseEntity<UserResponse> response = userServiceClient.getUserByEmail(email);
        if (response.getBody() == null) {
            throw new RuntimeException("Utilisateur non trouvé avec email: " + email);
        }
        return response.getBody();
    }

    /**
     * Vérifie si un utilisateur existe via le user-service centralisé
     */
    public boolean userExists(UUID userId) {
        try {
            ResponseEntity<Boolean> response = userServiceClient.userExists(userId);
            return Boolean.TRUE.equals(response.getBody());
        } catch (Exception e) {
            log.warn("Error checking user existence: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Récupère ou crée un utilisateur via le user-service centralisé
     */
    public UserResponse getOrCreateUser(String firstName, String lastName, String email) {
        log.info("Get or create user via user-service: {} {} ({})", firstName, lastName, email);
        
        CreateUserRequest request = CreateUserRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .username(email.split("@")[0])
                .role("Client")
                .build();
        
        ResponseEntity<UserResponse> response = userServiceClient.getOrCreateUser(request);
        if (response.getBody() == null) {
            throw new RuntimeException("Failed to get or create user in user-service");
        }
        return response.getBody();
    }
}
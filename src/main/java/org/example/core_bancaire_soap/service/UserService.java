package org.example.core_bancaire_soap.service;

import jakarta.transaction.Transactional;
import org.example.core_bancaire_soap.entity.User;
import org.example.core_bancaire_soap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Create
    public User createUser(String firstName, String lastName, String email) {
        return userRepository.save(new User(firstName, lastName, email));
    }

    // Read
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // Update
    public User updateUser(Long id, String newEmail) {
        User user = getUser(id);
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    // Delete
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
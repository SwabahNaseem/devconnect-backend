// ─────────────────────────────────────────────────────────────
// UserRepository.java
// Spring Data JPA automatically implements all basic DB operations
// You don't write SQL — just define method names and Spring figures it out
// ─────────────────────────────────────────────────────────────
package com.devconnect.repository;

import com.devconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email — used during login
    // Spring generates: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
 
    List<User> findByNameContainingIgnoreCase(String name);

    // Check if email already exists — used during registration
    boolean existsByEmail(String email);
}
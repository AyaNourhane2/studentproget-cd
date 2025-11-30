package com.example.demo.repository;

import com.example.demo.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    
    // Trouver une université par son nom
    Optional<University> findByName(String name);
    
    // Vérifier si une université existe par nom
    boolean existsByName(String name);
}
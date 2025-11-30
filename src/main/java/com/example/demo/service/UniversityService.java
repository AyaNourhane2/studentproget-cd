package com.example.demo.service;

import com.example.demo.model.University;
import com.example.demo.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UniversityService {
    
    @Autowired
    private UniversityRepository universityRepository;
    
    // Récupérer toutes les universités
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }
    
    // Récupérer une université par ID
    public Optional<University> getUniversityById(Long id) {
        return universityRepository.findById(id);
    }
    
    // Créer une nouvelle université
    public University createUniversity(University university) {
        return universityRepository.save(university);
    }
    
    // Mettre à jour une université
    public University updateUniversity(Long id, University universityDetails) {
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()) {
            University university = optionalUniversity.get();
            university.setName(universityDetails.getName());
            university.setLocation(universityDetails.getLocation());
            university.setEstablishedYear(universityDetails.getEstablishedYear());
            university.setDescription(universityDetails.getDescription());
            return universityRepository.save(university);
        }
        return null;
    }
    
    // Supprimer une université
    public boolean deleteUniversity(Long id) {
        if (universityRepository.existsById(id)) {
            universityRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Trouver une université par nom
    public Optional<University> getUniversityByName(String name) {
        return universityRepository.findByName(name);
    }
}
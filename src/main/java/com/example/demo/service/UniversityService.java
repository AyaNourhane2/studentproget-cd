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
    
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }
    
    public Optional<University> getUniversityById(Long id) {
        return universityRepository.findById(id);
    }
    
    public University createUniversity(University university) {
        return universityRepository.save(university);
    }
    
    public University updateUniversity(Long id, University universityDetails) {
        Optional<University> optionalUniversity = universityRepository.findById(id);
        if (optionalUniversity.isPresent()) {
            University university = optionalUniversity.get();
            university.setName(universityDetails.getName());
            university.setLocation(universityDetails.getLocation());
            return universityRepository.save(university);
        }
        return null;
    }
    
    public boolean deleteUniversity(Long id) {
        if (universityRepository.existsById(id)) {
            universityRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Optional<University> getUniversityByName(String name) {
        return universityRepository.findByName(name);
    }
}
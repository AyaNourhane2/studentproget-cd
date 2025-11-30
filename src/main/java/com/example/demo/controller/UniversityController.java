package com.example.demo.controller;

import com.example.demo.model.University;
import com.example.demo.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/universities")
@CrossOrigin(origins = "*")
public class UniversityController {
    
    @Autowired
    private UniversityService universityService;
    
    @GetMapping
    public List<University> getAllUniversities() {
        return universityService.getAllUniversities();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable Long id) {
        Optional<University> university = universityService.getUniversityById(id);
        return university.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public University createUniversity(@RequestBody University university) {
        return universityService.createUniversity(university);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable Long id, @RequestBody University universityDetails) {
        University updatedUniversity = universityService.updateUniversity(id, universityDetails);
        return updatedUniversity != null ? ResponseEntity.ok(updatedUniversity) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUniversity(@PathVariable Long id) {
        boolean deleted = universityService.deleteUniversity(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<University> getUniversityByName(@RequestParam String name) {
        Optional<University> university = universityService.getUniversityByName(name);
        return university.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
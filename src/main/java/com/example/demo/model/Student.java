package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "students")
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "university_id")
    @JsonIgnoreProperties({"students"})
    private University university;
    
    // NOUVEAU: Champ transient pour recevoir universityId depuis le JSON
    @Transient
    @JsonProperty("universityId")
    private Long universityId;
    
    // Constructeurs
    public Student() {
        // Constructeur par défaut requis par JPA
    }
    
    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    
    public Student(String firstName, String lastName, String email, University university) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.university = university;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public University getUniversity() {
        return university;
    }
    
    public void setUniversity(University university) {
        this.university = university;
    }
    
    // NOUVEAU: Getter et Setter pour universityId
    public Long getUniversityId() {
        return universityId;
    }
    
    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }
    
    // Méthode utilitaire pour gérer l'université après désérialisation
    @PostLoad
    @PostPersist
    @PostUpdate
    public void updateUniversityIdFromUniversity() {
        if (this.university != null) {
            this.universityId = this.university.getId();
        }
    }
    
    // Méthode toString()
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", university=" + (university != null ? university.getName() : "null") +
                ", universityId=" + universityId +
                '}';
    }
}
package com.example.demo.repository;

import com.example.demo.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    // Search by name
    List<Student> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    
    // Search by email
    Optional<Student> findByEmail(String email);
    
    // Search by university name
    @Query("SELECT s FROM Student s JOIN s.university u WHERE u.name LIKE %:universityName%")
    List<Student> findByUniversityNameContaining(@Param("universityName") String universityName);
    
    // Search by multiple criteria
    @Query("SELECT s FROM Student s WHERE " +
           "(:firstName IS NULL OR s.firstName LIKE %:firstName%) AND " +
           "(:lastName IS NULL OR s.lastName LIKE %:lastName%) AND " +
           "(:email IS NULL OR s.email LIKE %:email%) AND " +
           "(:universityName IS NULL OR s.university.name LIKE %:universityName%)")
    List<Student> searchStudents(@Param("firstName") String firstName,
                                @Param("lastName") String lastName,
                                @Param("email") String email,
                                @Param("universityName") String universityName);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Find students by university ID
    List<Student> findByUniversityId(Long universityId);
}
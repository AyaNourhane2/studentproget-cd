package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.model.University;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UniversityRepository universityRepository;
    
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }
    
    public Student createStudent(Student student) {
        // Vérifier si l'email existe déjà
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }
        
        // Gérer la relation avec l'université
        if (student.getUniversity() != null && student.getUniversity().getId() != null) {
            Optional<University> universityOpt = universityRepository.findById(student.getUniversity().getId());
            if (universityOpt.isPresent()) {
                student.setUniversity(universityOpt.get());
            } else {
                throw new RuntimeException("University not found with id: " + student.getUniversity().getId());
            }
        } else {
            // Si pas d'université, mettre à null
            student.setUniversity(null);
        }
        
        return studentRepository.save(student);
    }
    
    public Student updateStudent(Long id, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            
            // Vérifier si l'email a changé et s'il existe déjà
            if (!student.getEmail().equals(studentDetails.getEmail()) && 
                studentRepository.existsByEmail(studentDetails.getEmail())) {
                throw new RuntimeException("Email already exists: " + studentDetails.getEmail());
            }
            
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            student.setEmail(studentDetails.getEmail());
            
            // Gérer la mise à jour de l'université
            if (studentDetails.getUniversity() != null && studentDetails.getUniversity().getId() != null) {
                Optional<University> universityOpt = universityRepository.findById(studentDetails.getUniversity().getId());
                if (universityOpt.isPresent()) {
                    student.setUniversity(universityOpt.get());
                } else {
                    throw new RuntimeException("University not found with id: " + studentDetails.getUniversity().getId());
                }
            } else {
                student.setUniversity(null);
            }
            
            return studentRepository.save(student);
        }
        return null;
    }
    
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Student> searchStudents(String firstName, String lastName, String email, String universityName) {
        return studentRepository.searchStudents(firstName, lastName, email, universityName);
    }
    
    public List<Student> findByUniversityName(String universityName) {
        return studentRepository.findByUniversityNameContaining(universityName);
    }
    
    public List<Student> findByUniversityId(Long universityId) {
        return studentRepository.findByUniversityId(universityId);
    }
    
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }
    
    public Optional<Student> getStudentByEmail(String email) {
        return studentRepository.findByEmail(email);
    }
}
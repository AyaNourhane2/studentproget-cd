package com.example.demo.service;

import com.example.demo.model.Student;
import com.example.demo.model.University;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private UniversityRepository universityRepository;
    
    // Récupérer tous les étudiants
    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        // Mettre à jour universityId pour chaque étudiant
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    // Récupérer un étudiant par ID
    public Optional<Student> getStudentById(Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        studentOpt.ifPresent(Student::updateUniversityIdFromUniversity);
        return studentOpt;
    }
    
    // Créer un nouvel étudiant
    public Student createStudent(Student student) {
        // Vérifier si l'email existe déjà
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }
        
        // Gérer la relation avec l'université via universityId
        if (student.getUniversityId() != null && student.getUniversityId() > 0) {
            Optional<University> universityOpt = universityRepository.findById(student.getUniversityId());
            if (universityOpt.isPresent()) {
                student.setUniversity(universityOpt.get());
            } else {
                throw new RuntimeException("University not found with id: " + student.getUniversityId());
            }
        } else {
            student.setUniversity(null);
        }
        
        // Sauvegarder l'étudiant
        Student savedStudent = studentRepository.save(student);
        
        // 🔥 CORRECTION : Recharger l'étudiant pour s'assurer d'avoir l'université
        Optional<Student> reloadedStudentOpt = studentRepository.findById(savedStudent.getId());
        if (reloadedStudentOpt.isPresent()) {
            savedStudent = reloadedStudentOpt.get();
        }
        
        // S'assurer que universityId est défini
        savedStudent.setUniversityId(student.getUniversityId());
        return savedStudent;
    }
    
    // Mettre à jour un étudiant
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
            if (studentDetails.getUniversityId() != null && studentDetails.getUniversityId() > 0) {
                Optional<University> universityOpt = universityRepository.findById(studentDetails.getUniversityId());
                if (universityOpt.isPresent()) {
                    student.setUniversity(universityOpt.get());
                } else {
                    throw new RuntimeException("University not found with id: " + studentDetails.getUniversityId());
                }
            } else {
                student.setUniversity(null);
            }
            
            Student updatedStudent = studentRepository.save(student);
            
            // Recharger pour avoir l'université
            Optional<Student> reloadedStudentOpt = studentRepository.findById(updatedStudent.getId());
            if (reloadedStudentOpt.isPresent()) {
                updatedStudent = reloadedStudentOpt.get();
            }
            
            updatedStudent.setUniversityId(studentDetails.getUniversityId());
            return updatedStudent;
        }
        return null;
    }
    
    // Supprimer un étudiant
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Recherche d'étudiants
    public List<Student> searchStudents(String firstName, String lastName, String email, String universityName) {
        List<Student> students = studentRepository.searchStudents(firstName, lastName, email, universityName);
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    // Trouver par nom d'université
    public List<Student> findByUniversityName(String universityName) {
        List<Student> students = studentRepository.findByUniversityNameContaining(universityName);
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    // Trouver par ID d'université
    public List<Student> findByUniversityId(Long universityId) {
        List<Student> students = studentRepository.findByUniversityId(universityId);
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    // Vérifier si l'email existe
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }
    
    // Trouver par email
    public Optional<Student> getStudentByEmail(String email) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        studentOpt.ifPresent(Student::updateUniversityIdFromUniversity);
        return studentOpt;
    }
}
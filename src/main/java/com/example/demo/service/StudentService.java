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
        List<Student> students = studentRepository.findAll();
        // Mettre à jour universityId pour chaque étudiant
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    public Optional<Student> getStudentById(Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        studentOpt.ifPresent(Student::updateUniversityIdFromUniversity);
        return studentOpt;
    }
    
    public Student createStudent(Student student) {
        // Vérifier si l'email existe déjà
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }
        
        // 🔥 CORRECTION : Gérer l'université via universityId
        University university = null;
        if (student.getUniversityId() != null && student.getUniversityId() > 0) {
            Optional<University> universityOpt = universityRepository.findById(student.getUniversityId());
            if (universityOpt.isPresent()) {
                university = universityOpt.get();
                student.setUniversity(university);
            } else {
                throw new RuntimeException("University not found with id: " + student.getUniversityId());
            }
        } else {
            student.setUniversity(null);
        }
        
        // Sauvegarder l'étudiant
        Student savedStudent = studentRepository.save(student);
        
        // 🔥 CORRECTION CRITIQUE : Recharger l'étudiant pour avoir l'université
        Optional<Student> reloadedStudentOpt = studentRepository.findById(savedStudent.getId());
        if (reloadedStudentOpt.isPresent()) {
            savedStudent = reloadedStudentOpt.get();
        }
        
        // S'assurer que universityId est défini dans la réponse
        savedStudent.setUniversityId(student.getUniversityId());
        
        return savedStudent;
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
            
            // 🔥 CORRECTION : Gérer la mise à jour de l'université
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
    
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Student> searchStudents(String firstName, String lastName, String email, String universityName) {
        List<Student> students = studentRepository.searchStudents(firstName, lastName, email, universityName);
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    public List<Student> findByUniversityName(String universityName) {
        List<Student> students = studentRepository.findByUniversityNameContaining(universityName);
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    public List<Student> findByUniversityId(Long universityId) {
        List<Student> students = studentRepository.findByUniversityId(universityId);
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    public boolean emailExists(String email) {
        return studentRepository.existsByEmail(email);
    }
    
    public Optional<Student> getStudentByEmail(String email) {
        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        studentOpt.ifPresent(Student::updateUniversityIdFromUniversity);
        return studentOpt;
    }
}
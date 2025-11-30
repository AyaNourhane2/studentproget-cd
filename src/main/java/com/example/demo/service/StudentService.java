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
    
    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        students.forEach(Student::updateUniversityIdFromUniversity);
        return students;
    }
    
    public Optional<Student> getStudentById(Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        studentOpt.ifPresent(Student::updateUniversityIdFromUniversity);
        return studentOpt;
    }
    
    public Student createStudent(Student student) {
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new RuntimeException("Email already exists: " + student.getEmail());
        }
        
        if (student.getUniversityId() != null && student.getUniversityId() > 0) {
            Optional<University> universityOpt = universityRepository.findById(student.getUniversityId());
            if (universityOpt.isPresent()) {
                student.setUniversity(universityOpt.get());
            }
        }
        
        Student savedStudent = studentRepository.save(student);
        savedStudent.updateUniversityIdFromUniversity();
        return savedStudent;
    }
    
    public Student updateStudent(Long id, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            
            if (!student.getEmail().equals(studentDetails.getEmail()) && 
                studentRepository.existsByEmail(studentDetails.getEmail())) {
                throw new RuntimeException("Email already exists: " + studentDetails.getEmail());
            }
            
            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            student.setEmail(studentDetails.getEmail());
            
            if (studentDetails.getUniversityId() != null && studentDetails.getUniversityId() > 0) {
                Optional<University> universityOpt = universityRepository.findById(studentDetails.getUniversityId());
                if (universityOpt.isPresent()) {
                    student.setUniversity(universityOpt.get());
                }
            } else {
                student.setUniversity(null);
            }
            
            Student updatedStudent = studentRepository.save(student);
            updatedStudent.updateUniversityIdFromUniversity();
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
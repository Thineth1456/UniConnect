package edu.uok.service;

import edu.uok.dto.AuthResponse;
import edu.uok.dto.StudentSignUpRequest;
import edu.uok.exception.ApiException;
import edu.uok.model.AccountStatus;
import edu.uok.model.Role;
import edu.uok.model.Student;
import edu.uok.model.User;
import edu.uok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthResponse signUpStudent(StudentSignUpRequest req) {
        String email = normalize(req.email());
        ensureEmailIsFree(email);

        Student student = new Student();
        fillCommonFields(student, req.name(), email, req.password(), req.address(), Role.STUDENT);
        student.setStatus(AccountStatus.ACTIVE);

        student.setStudentId(req.studentId().trim());
        student.setFaculty(req.faculty().trim());
        student.setDepartment(req.department().trim());
        student.setDegree(req.degree().trim());
        student.setBatch(req.batch().trim());
        student.setSkills(listOrEmpty(req.skills()));
        student.setWorkMode(listOrEmpty(req.workMode()));
        student.setPreferredCategories(listOrEmpty(req.preferredCategories()));
        student.setExpectedSalary(req.expectedSalary());

        userRepository.save(student);
        return new AuthResponse( email,"Student account created", student);
    }
    private String normalize(String email) {
        return email.trim().toLowerCase();
    }

    private void ensureEmailIsFree(String email) {
        if (userRepository.existsByEmail(email)) {

            throw new ApiException(HttpStatus.CONFLICT, "An account with this email already exists");
        }
    }

    private void fillCommonFields(User user, String name, String email, String rawPassword,
                                  String address, Role role) {
        user.setName(name.trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setVerified(false);
        user.setAddress(address);
    }

    private List<String> listOrEmpty(List<String> list) {
        return list == null ? new ArrayList<>() : new ArrayList<>(list);
    }
}

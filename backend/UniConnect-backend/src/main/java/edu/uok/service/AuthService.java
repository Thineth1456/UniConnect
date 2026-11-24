package edu.uok.service;

import edu.uok.dto.AuthResponse;
import edu.uok.dto.JobGiverSignUpRequest;
import edu.uok.dto.StudentSignUpRequest;
import edu.uok.exception.ApiException;
import edu.uok.model.*;
import edu.uok.repository.UserRepository;
import edu.uok.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthService {

    private static final Set<Role> JOB_GIVER_ROLES = Set.of(Role.EMPLOYER, Role.LECTURER, Role.ALUMNUS);


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.job-giver-auto-approve:true}")
    private boolean autoApproveJobGiver;


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
        return new AuthResponse(jwtService.generateToken(student),"Student account created", student);
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

    public AuthResponse signUpJobGiver(JobGiverSignUpRequest req) {
        if(!JOB_GIVER_ROLES.contains(req.role())){
            throw new ApiException(HttpStatus.BAD_REQUEST,"Role must be EMPLOYER, LECTURER or ALUMNUS");
        }
        String email = normalize(req.email());
        ensureEmailIsFree(email);

        JobGiver giver = new JobGiver();
        fillCommonFields(giver, req.name(),email, req.password(),req.address(),req.role());
        giver.setStatus(autoApproveJobGiver? AccountStatus.ACTIVE:AccountStatus.PENDING);

        giver.setOrganization(req.organization().trim());
        giver.setPosition(req.position().trim());
        giver.setContactNumber(req.contactNumber().trim());
         userRepository.save(giver);

         if(giver.getStatus() == AccountStatus.ACTIVE){
             return new AuthResponse(jwtService.generateToken(giver),"Account created",giver);
         }
         return new AuthResponse(null,"Account create. You can sign in after an administrator approves it.",giver);
    }
}

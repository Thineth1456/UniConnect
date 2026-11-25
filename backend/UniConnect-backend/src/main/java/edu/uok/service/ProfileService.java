package edu.uok.service;

import edu.uok.exception.ApiException;
import edu.uok.model.Student;
import edu.uok.model.User;
import edu.uok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final UserRepository userRepository;

    public Student getStudent(String email){
        return findStudent(email);
    }

    private Student findStudent(String email){
        User user = findUser(email);
        if (user instanceof Student student){
            return student;
        }
        throw new ApiException(HttpStatus.FORBIDDEN,"This account is not a student");
    }
    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }
}

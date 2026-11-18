package edu.uok.controller;

import edu.uok.dto.AuthResponse;
import edu.uok.dto.StudentSignUpRequest;
import edu.uok.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/student/signup")
    public ResponseEntity<AuthResponse> signUpStudent(@Valid @RequestBody StudentSignUpRequest request) {
        System.out.println("request come to here");
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signUpStudent(request));
    }

}

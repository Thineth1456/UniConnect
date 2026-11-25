package edu.uok.controller;

import edu.uok.model.Student;
import edu.uok.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/students/me")
    @PreAuthorize("hasRole('STUDENT')")
    public Student getStudentProfile(@AuthenticationPrincipal UserDetails principal) {
        return profileService.getStudent(principal.getUsername());
    }

}

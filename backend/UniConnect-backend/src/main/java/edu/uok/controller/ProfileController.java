package edu.uok.controller;

import edu.uok.dto.UpdateJobGiverProfileRequest;
import edu.uok.dto.UpdateStudentProfileRequest;
import edu.uok.model.JobGiver;
import edu.uok.model.Student;
import edu.uok.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PatchMapping("/students/me")
    @PreAuthorize("hasRole('STUDENT')")
    public Student updateStudentProfile(@AuthenticationPrincipal UserDetails principal,
                                        @Valid @RequestBody UpdateStudentProfileRequest request) {
        return profileService.updateStudent(principal.getUsername(), request);
    }

    @GetMapping("/job-givers/me")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'LECTURER', 'ALUMNUS')")
    public JobGiver getJobGiverProfile(@AuthenticationPrincipal UserDetails principal) {
        return profileService.getJobGiver(principal.getUsername());
    }

    @PatchMapping("/job-givers/me")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'LECTURER', 'ALUMNUS')")
    public JobGiver updateJobGiverProfile(@AuthenticationPrincipal UserDetails principal,
                                          @Valid @RequestBody UpdateJobGiverProfileRequest request) {
        return profileService.updateJobGiver(principal.getUsername(), request);
    }

}

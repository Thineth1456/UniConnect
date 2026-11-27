package edu.uok.service;

import edu.uok.model.*;
import edu.uok.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationStudentValidationTest {

    @Mock
    private JobApplicationRepository applicationRepository;

    @Mock
    private VacancyRepository vacancyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobApplicationService applicationService;

    private Student createStudent(Role role, AccountStatus status) {
        Student student = new Student();
        student.setId("student-123");
        student.setRole(role);
        student.setStatus(status);
        return student;
    }

    @Test
    void shouldRejectNonexistentStudent() {
        when(userRepository.findById("missing-student"))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                applicationService.submitApplication(
                        "vacancy-123", "missing-student", null, null));

        verifyNoInteractions(vacancyRepository, applicationRepository);
    }

    @Test
    void shouldRejectNonStudentRole() {
        Student user = createStudent(Role.EMPLOYER, AccountStatus.ACTIVE);

        when(userRepository.findById("student-123"))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class, () ->
                applicationService.submitApplication(
                        "vacancy-123", "student-123", null, null));

        verifyNoInteractions(vacancyRepository, applicationRepository);
    }

    @Test
    void shouldRejectSuspendedStudent() {
        Student student = createStudent(
                Role.STUDENT, AccountStatus.SUSPENDED);

        when(userRepository.findById("student-123"))
                .thenReturn(Optional.of(student));

        assertThrows(IllegalStateException.class, () ->
                applicationService.submitApplication(
                        "vacancy-123", "student-123", null, null));

        verifyNoInteractions(vacancyRepository, applicationRepository);
    }

    @Test
    void shouldAllowActiveStudentToApply() {
        Student student = createStudent(
                Role.STUDENT, AccountStatus.ACTIVE);

        Vacancy vacancy = new Vacancy();
        vacancy.setId("vacancy-123");
        vacancy.setStatus("APPROVED");
        vacancy.setApplicationDeadline(
                Instant.now().plusSeconds(86400));

        when(userRepository.findById("student-123"))
                .thenReturn(Optional.of(student));

        when(vacancyRepository.findById("vacancy-123"))
                .thenReturn(Optional.of(vacancy));

        when(applicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = applicationService.submitApplication(
                "vacancy-123", "student-123", null, null);

        assertEquals("SUBMITTED", result.getStatus());
        assertEquals("student-123", result.getStudentId());

        verify(applicationRepository).save(any(JobApplication.class));
    }
}
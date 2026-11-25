package edu.uok.service;

import edu.uok.model.AccountStatus;
import edu.uok.model.Role;
import edu.uok.model.Student;
import edu.uok.repository.UserRepository;import edu.uok.model.JobApplication;
import edu.uok.model.Vacancy;
import edu.uok.repository.JobApplicationRepository;
import edu.uok.repository.VacancyRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository applicationRepository;

    @Mock
    private VacancyRepository vacancyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JobApplicationService applicationService;

    private Vacancy vacancy;

    @BeforeEach
    void setUp() {
        Student student = new Student();
        student.setId("student-123");
        student.setRole(Role.STUDENT);
        student.setStatus(AccountStatus.ACTIVE);

        when(userRepository.findById("student-123"))
                .thenReturn(Optional.of(student));
        vacancy = new Vacancy();
        vacancy.setId("vacancy-123");
        vacancy.setStatus("APPROVED");
        vacancy.setApplicationDeadline(
                Instant.now().plusSeconds(86400)
        );
    }

    @Test
    void shouldSubmitApplicationSuccessfully() {
        when(vacancyRepository.findById("vacancy-123"))
                .thenReturn(Optional.of(vacancy));

        when(applicationRepository.existsByVacancyIdAndStudentId(
                "vacancy-123", "student-123"))
                .thenReturn(false);

        when(applicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = applicationService.submitApplication(
                "vacancy-123",
                "student-123",
                "https://example.com/cv.pdf",
                "I am interested in this internship."
        );

        assertEquals("SUBMITTED", result.getStatus());
        assertEquals("vacancy-123", result.getVacancyId());
        assertEquals("student-123", result.getStudentId());

        verify(applicationRepository).save(any(JobApplication.class));
    }

    @Test
    void shouldRejectDuplicateApplication() {
        when(vacancyRepository.findById("vacancy-123"))
                .thenReturn(Optional.of(vacancy));

        when(applicationRepository.existsByVacancyIdAndStudentId(
                "vacancy-123", "student-123"))
                .thenReturn(true);

        assertThrows(
                IllegalStateException.class,
                () -> applicationService.submitApplication(
                        "vacancy-123", "student-123", null, null
                )
        );

        verify(applicationRepository, never())
                .save(any(JobApplication.class));
    }

    @Test
    void shouldRejectUnapprovedVacancy() {
        vacancy.setStatus("DRAFT");

        when(vacancyRepository.findById("vacancy-123"))
                .thenReturn(Optional.of(vacancy));

        assertThrows(
                IllegalStateException.class,
                () -> applicationService.submitApplication(
                        "vacancy-123", "student-123", null, null
                )
        );

        verify(applicationRepository, never())
                .save(any(JobApplication.class));
    }

    @Test
    void shouldRejectExpiredVacancy() {
        vacancy.setApplicationDeadline(
                Instant.now().minusSeconds(86400)
        );

        when(vacancyRepository.findById("vacancy-123"))
                .thenReturn(Optional.of(vacancy));

        assertThrows(
                IllegalStateException.class,
                () -> applicationService.submitApplication(
                        "vacancy-123", "student-123", null, null
                )
        );

        verify(applicationRepository, never())
                .save(any(JobApplication.class));
    }

    @Test
    void shouldRejectNonexistentVacancy() {
        when(vacancyRepository.findById("missing-vacancy"))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> applicationService.submitApplication(
                        "missing-vacancy", "student-123", null, null
                )
        );

        verify(applicationRepository, never())
                .save(any(JobApplication.class));
    }
}
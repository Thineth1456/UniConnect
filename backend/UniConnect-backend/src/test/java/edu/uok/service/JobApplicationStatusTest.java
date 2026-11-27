package edu.uok.service;

import edu.uok.model.ApplicationStatus;
import edu.uok.model.JobApplication;
import edu.uok.repository.JobApplicationRepository;
import edu.uok.repository.VacancyRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationStatusTest {

    @Mock
    private JobApplicationRepository applicationRepository;

    @Mock
    private VacancyRepository vacancyRepository;

    @InjectMocks
    private JobApplicationService applicationService;

    private JobApplication createApplication(String status) {
        JobApplication application = new JobApplication();
        application.setId("application-123");
        application.setVacancyId("vacancy-123");
        application.setStudentId("student-123");
        application.setStatus(status);
        application.setUpdatedAt(Instant.now().minusSeconds(3600));
        return application;
    }

    @Test
    void shouldMoveSubmittedToUnderReview() {
        JobApplication application = createApplication("SUBMITTED");

        when(applicationRepository.findById("application-123"))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = applicationService.updateApplicationStatus(
                "application-123", ApplicationStatus.UNDER_REVIEW);

        assertEquals("UNDER_REVIEW", result.getStatus());
        verify(applicationRepository).save(application);
    }

    @Test
    void shouldMoveUnderReviewToShortlisted() {
        JobApplication application = createApplication("UNDER_REVIEW");

        when(applicationRepository.findById("application-123"))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = applicationService.updateApplicationStatus(
                "application-123", ApplicationStatus.SHORTLISTED);

        assertEquals("SHORTLISTED", result.getStatus());
        verify(applicationRepository).save(application);
    }

    @Test
    void shouldMoveShortlistedToHired() {
        JobApplication application = createApplication("SHORTLISTED");

        when(applicationRepository.findById("application-123"))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = applicationService.updateApplicationStatus(
                "application-123", ApplicationStatus.HIRED);

        assertEquals("HIRED", result.getStatus());
        verify(applicationRepository).save(application);
    }

    @Test
    void shouldRejectTransitionFromHiredToSubmitted() {
        JobApplication application = createApplication("HIRED");

        when(applicationRepository.findById("application-123"))
                .thenReturn(Optional.of(application));

        assertThrows(IllegalStateException.class, () ->
                applicationService.updateApplicationStatus(
                        "application-123", ApplicationStatus.SUBMITTED));

        verify(applicationRepository, never())
                .save(any(JobApplication.class));
    }

    @Test
    void shouldRejectNullStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                applicationService.updateApplicationStatus(
                        "application-123", null));

        verifyNoInteractions(applicationRepository);
    }

    @Test
    void shouldUpdateTimestampOnSuccessfulTransition() {
        JobApplication application = createApplication("SUBMITTED");
        Instant previousTimestamp = application.getUpdatedAt();

        when(applicationRepository.findById("application-123"))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication result = applicationService.updateApplicationStatus(
                "application-123", ApplicationStatus.UNDER_REVIEW);

        assertTrue(result.getUpdatedAt().isAfter(previousTimestamp));
        verify(applicationRepository).save(application);
    }
}
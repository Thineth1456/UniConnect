package edu.uok.service;

import edu.uok.model.AccountStatus;
import edu.uok.model.ApplicationStatus;
import edu.uok.model.Role;
import edu.uok.model.User;
import edu.uok.repository.UserRepository;
import edu.uok.model.JobApplication;
import edu.uok.model.Vacancy;
import edu.uok.repository.JobApplicationRepository;
import edu.uok.repository.VacancyRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;

    public JobApplication submitApplication(
            String vacancyId,
            String studentId,
            String cvUrl,
            String coverLetter
    ) {
        // Validate required identifiers
        if (vacancyId == null || vacancyId.isBlank()) {
            throw new IllegalArgumentException(
                    "Vacancy ID is required"
            );
        }

        if (studentId == null || studentId.isBlank()) {
            throw new IllegalArgumentException(
                    "Student ID is required"
            );
        }

        // Validate the student account
        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Student account not found"
                        )
                );

        if (student.getRole() != Role.STUDENT) {
            throw new IllegalStateException(
                    "Only students can submit job applications"
            );
        }

        if (student.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Student account is not active"
            );
        }
        // Check whether the vacancy exists
        Vacancy vacancy = vacancyRepository
                .findById(vacancyId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Vacancy not found"
                        )
                );

        // Only approved vacancies accept applications
        if (!"APPROVED".equals(vacancy.getStatus())) {
            throw new IllegalStateException(
                    "This vacancy is not open for applications"
            );
        }

        // Check application deadline
        if (vacancy.getApplicationDeadline() != null
                && !vacancy.getApplicationDeadline()
                        .isAfter(Instant.now())) {

            throw new IllegalStateException(
                    "Application deadline has passed"
            );
        }

        // Prevent duplicate submissions
        boolean alreadyApplied = applicationRepository
                .existsByVacancyIdAndStudentId(
                        vacancyId,
                        studentId
                );

        if (alreadyApplied) {
            throw new IllegalStateException(
                    "Student has already applied for this vacancy"
            );
        }

        // Create the application
        JobApplication application = new JobApplication();

        application.setVacancyId(vacancyId);
        application.setStudentId(studentId);
        application.setCvUrl(cvUrl);
        application.setCoverLetter(coverLetter);
        application.setStatus("SUBMITTED");
        application.setAppliedAt(Instant.now());
        application.setUpdatedAt(Instant.now());

        // Save application to MongoDB
        return applicationRepository.save(application);
    }

    public List<JobApplication> getApplicationsByStudent(
            String studentId
    ) {
        return applicationRepository.findByStudentId(studentId);
    }

    public List<JobApplication> getApplicationsByVacancy(
            String vacancyId
    ) {
        return applicationRepository.findByVacancyId(vacancyId);
    }

    public JobApplication getApplicationById(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Application not found"
                        )
                );
    }

    public JobApplication updateApplicationStatus(
            String applicationId,
            ApplicationStatus newStatus
    ) {
        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "Application status is required"
            );
        }

        JobApplication application =
                getApplicationById(applicationId);

        ApplicationStatus currentStatus =
                ApplicationStatus.valueOf(application.getStatus());

        boolean allowed = switch (currentStatus) {
            case SUBMITTED ->
                    newStatus == ApplicationStatus.UNDER_REVIEW
                    || newStatus == ApplicationStatus.REJECTED
                    || newStatus == ApplicationStatus.WITHDRAWN;

            case UNDER_REVIEW ->
                    newStatus == ApplicationStatus.SHORTLISTED
                    || newStatus == ApplicationStatus.REJECTED
                    || newStatus == ApplicationStatus.WITHDRAWN;

            case SHORTLISTED ->
                    newStatus == ApplicationStatus.HIRED
                    || newStatus == ApplicationStatus.REJECTED
                    || newStatus == ApplicationStatus.WITHDRAWN;

            case HIRED, REJECTED, WITHDRAWN -> false;
        };

        if (!allowed) {
            throw new IllegalStateException(
                    "Invalid application status transition: "
                    + currentStatus + " to " + newStatus
            );
        }

        application.setStatus(newStatus.name());
        application.setUpdatedAt(Instant.now());

        return applicationRepository.save(application);
    }
}
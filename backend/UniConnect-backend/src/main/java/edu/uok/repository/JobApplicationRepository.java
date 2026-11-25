package edu.uok.repository;

import edu.uok.model.JobApplication;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository
        extends MongoRepository<JobApplication, String> {

    // Find all applications submitted by a student
    List<JobApplication> findByStudentId(String studentId);

    // Find all applications for a specific vacancy
    List<JobApplication> findByVacancyId(String vacancyId);

    // Check if a student has already applied
    boolean existsByVacancyIdAndStudentId(
            String vacancyId,
            String studentId
    );

    // Find a student's application for a specific vacancy
    Optional<JobApplication> findByVacancyIdAndStudentId(
            String vacancyId,
            String studentId
    );

    // Find applications by their review status
    List<JobApplication> findByStatus(String status);
}
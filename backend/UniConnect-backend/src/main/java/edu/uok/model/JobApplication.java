package edu.uok.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@Document(collection = "job_applications")
@CompoundIndex(name = "unique_student_vacancy", def = "{'vacancyId': 1, 'studentId': 1}", unique = true)
public class JobApplication {

    @Id
    private String id;

    @NotBlank(message = "Vacancy ID is required")
    private String vacancyId;

    @NotBlank(message = "Student ID is required")
    private String studentId;

    private String cvUrl;

    private String coverLetter;

    // SUBMITTED, UNDER_REVIEW, SHORTLISTED,
    // REJECTED, WITHDRAWN, HIRED
    private String status = "SUBMITTED";

    private Instant appliedAt = Instant.now();

    private Instant updatedAt = Instant.now();
}
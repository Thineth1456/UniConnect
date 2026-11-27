
package edu.uok.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Future;


@Getter
@Setter
@Document(collection = "vacancies")
public class Vacancy {

    @Id
    private String id;

    @NotBlank(message = "Job title is required")
    private String title;
    @NotBlank(message = "Job description is required")
    private String description;
    @NotBlank(message = "Company name is required")
   
    private String companyName;

    // PART_TIME or INTERNSHIP
    private String employmentType;

    // ONSITE, REMOTE or HYBRID
    private String workMode;

    private String location;

    // Salary in LKR
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", message = "Salary cannot be negative")
    private Double salary;
    private String salaryPeriod;

    // Working hours
    @NotNull(message = "Working hours are required")
    @Min(value = 1, message = "Working hours must be greater than zero")
    private Integer hoursPerWeek;

    // Required skills
    private List<String> requiredSkills = new ArrayList<>();

    // Lecturer-targeted opportunities
    private String targetDepartment;
    private String targetDegree;
    private String targetBatch;

    // References the User.id of the vacancy creator
    private String postedByUserId;

    // DRAFT, PENDING_APPROVAL, APPROVED, REJECTED or CLOSED
    private String status = "DRAFT";

    @Future(message = "Application deadline must be in the future")
    private Instant applicationDeadline;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}

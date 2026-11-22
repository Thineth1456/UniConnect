
package edu.uok.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "vacancies")
public class Vacancy {

    @Id
    private String id;

    private String title;
    private String description;
    private String companyName;

    // PART_TIME or INTERNSHIP
    private String employmentType;

    // ONSITE, REMOTE or HYBRID
    private String workMode;

    private String location;

    // Salary in LKR
    private Double salary;
    private String salaryPeriod;

    // Working hours
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

    private Instant applicationDeadline;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}

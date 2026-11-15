package edu.uok.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "users")
public class Student extends User {

    private String studentId;
    private String faculty;
    private String department;
    private String degree;
    private String batch;

    private List<String> skills = new ArrayList<>();
    private String cvUrl;
    private List<String> workMode = new ArrayList<>();          // e.g. ONSITE, REMOTE, HYBRID
    private List<String> preferredCategories = new ArrayList<>();
    private Double expectedSalary;
    private boolean optedOutOfMatching = false;
}

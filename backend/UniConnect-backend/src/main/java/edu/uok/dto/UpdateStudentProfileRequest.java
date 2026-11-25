package edu.uok.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Every field is optional. A field that is not sent (null) is left unchanged.
 * Email and studentId cannot be changed here.
 */
public record UpdateStudentProfileRequest(
        @Size(min = 2, max = 100) String name,
        @Size(max = 255) String address,
        @Size(max = 100) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String faculty,
        @Size(max = 100) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String department,
        @Size(max = 100) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String degree,
        @Size(max = 20) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String batch,
        List<String> skills,
        List<String> workMode,
        List<String> preferredCategories,
        @PositiveOrZero Double expectedSalary,
        Boolean optedOutOfMatching) {
}

package edu.uok.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record StudentSignUpRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 64) String password,
        String address,
        @NotBlank String studentId,
        @NotBlank String faculty,
        @NotBlank String department,
        @NotBlank String degree,
        @NotBlank String batch,
        List<String> skills,
        List<String> workMode,
        List<String> preferredCategories,
        @PositiveOrZero Double expectedSalary) {
}
package edu.uok.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class VacancyValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setup() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void cleanup() {
        factory.close();
    }

    private Vacancy validVacancy() {
        Vacancy vacancy = new Vacancy();
        vacancy.setTitle("Software Engineering Intern");
        vacancy.setDescription("Develop and test applications");
        vacancy.setCompanyName("Example Company");
        vacancy.setSalary(30000.0);
        vacancy.setHoursPerWeek(40);
        vacancy.setApplicationDeadline(
                Instant.now().plusSeconds(86400)
        );
        return vacancy;
    }

    @Test
    void validVacancyShouldPass() {
        Vacancy vacancy = validVacancy();

        assertTrue(validator.validate(vacancy).isEmpty());
    }

    @Test
    void emptyTitleShouldFail() {
        Vacancy vacancy = validVacancy();
        vacancy.setTitle("");

        assertFalse(validator.validate(vacancy).isEmpty());
    }

    @Test
    void negativeSalaryShouldFail() {
        Vacancy vacancy = validVacancy();
        vacancy.setSalary(-1000.0);

        assertFalse(validator.validate(vacancy).isEmpty());
    }

    @Test
    void zeroWorkingHoursShouldFail() {
        Vacancy vacancy = validVacancy();
        vacancy.setHoursPerWeek(0);

        assertFalse(validator.validate(vacancy).isEmpty());
    }

    @Test
    void pastDeadlineShouldFail() {
        Vacancy vacancy = validVacancy();
        vacancy.setApplicationDeadline(
                Instant.now().minusSeconds(86400)
        );

        assertFalse(validator.validate(vacancy).isEmpty());
    }
}
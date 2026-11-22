
package edu.uok.controller;

import edu.uok.model.Vacancy;
import edu.uok.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    // GET /api/vacancies
    // List approved vacancies only
    @GetMapping
    public ResponseEntity<List<Vacancy>> getApprovedVacancies() {
        return ResponseEntity.ok(
                vacancyService.getApprovedVacancies()
        );
    }

    // GET /api/vacancies/{id}
    // Return vacancy details only if approved
    @GetMapping("/{id}")
    public ResponseEntity<Vacancy> getVacancyById(
            @PathVariable String id) {

        Vacancy vacancy = vacancyService.getVacancyById(id);

        if (!"APPROVED".equals(vacancy.getStatus())) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(vacancy);
    }
}

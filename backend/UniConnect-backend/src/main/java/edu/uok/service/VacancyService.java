
package edu.uok.service;

import edu.uok.model.Vacancy;
import edu.uok.repository.VacancyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VacancyService {

    private final VacancyRepository vacancyRepository;

    public VacancyService(VacancyRepository vacancyRepository) {
        this.vacancyRepository = vacancyRepository;
    }

    // Create a vacancy
    // Do not use this method directly from a public controller
    // until the current user's identity is verified.
    public Vacancy createVacancy(Vacancy vacancy) {
        vacancy.setId(null);
        vacancy.setStatus("DRAFT");
        vacancy.setCreatedAt(Instant.now());
        vacancy.setUpdatedAt(Instant.now());

        return vacancyRepository.save(vacancy);
    }

    // Retrieve all vacancies (internal/admin use)
    public List<Vacancy> getAllVacancies() {
        return vacancyRepository.findAll();
    }

    // Retrieve a vacancy by ID
    public Vacancy getVacancyById(String id) {
        return vacancyRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Vacancy not found: " + id
                        ));
    }

    // Retrieve approved vacancies
    public List<Vacancy> getApprovedVacancies() {
        return vacancyRepository.findByStatus("APPROVED");
    }

    // Retrieve vacancies posted by a user
    public List<Vacancy> getVacanciesByUser(String userId) {
        return vacancyRepository.findByPostedByUserId(userId);
    }

    // Update an existing vacancy
    // Caller must verify ownership/authorization first.
    public Vacancy updateVacancy(String id, Vacancy updated) {
        Vacancy existing = getVacancyById(id);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setCompanyName(updated.getCompanyName());
        existing.setEmploymentType(updated.getEmploymentType());
        existing.setWorkMode(updated.getWorkMode());
        existing.setLocation(updated.getLocation());
        existing.setSalary(updated.getSalary());
        existing.setSalaryPeriod(updated.getSalaryPeriod());
        existing.setHoursPerWeek(updated.getHoursPerWeek());
        existing.setRequiredSkills(updated.getRequiredSkills());
        existing.setTargetDepartment(updated.getTargetDepartment());
        existing.setTargetDegree(updated.getTargetDegree());
        existing.setTargetBatch(updated.getTargetBatch());
        existing.setApplicationDeadline(updated.getApplicationDeadline());

        // Edited vacancies must be reviewed again.
        existing.setStatus("DRAFT");
        existing.setUpdatedAt(Instant.now());

        return vacancyRepository.save(existing);
    }

    // Delete a vacancy
    // Caller must verify ownership/authorization first.
    public void deleteVacancy(String id) {
        Vacancy existing = getVacancyById(id);
        vacancyRepository.delete(existing);
    }
}

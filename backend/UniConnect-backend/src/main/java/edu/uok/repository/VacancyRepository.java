
package edu.uok.repository;

import edu.uok.model.Vacancy;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VacancyRepository extends MongoRepository<Vacancy, String> {

    // Find vacancies by approval status
    List<Vacancy> findByStatus(String status);

    // Find vacancies created by a specific user
    List<Vacancy> findByPostedByUserId(String postedByUserId);

    // Find vacancies by employment type
    List<Vacancy> findByEmploymentType(String employmentType);

    // Find vacancies targeting a specific department
    List<Vacancy> findByTargetDepartment(String targetDepartment);

    // Find vacancies by status and employment type
    List<Vacancy> findByStatusAndEmploymentType(
            String status,
            String employmentType
    );
}

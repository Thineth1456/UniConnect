package edu.uok.service;

import edu.uok.dto.UpdateJobGiverProfileRequest;
import edu.uok.dto.UpdateStudentProfileRequest;
import edu.uok.exception.ApiException;
import edu.uok.model.JobGiver;
import edu.uok.model.Student;
import edu.uok.model.User;
import edu.uok.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ProfileService {

    private static final Set<String> WORK_MODES = Set.of("ONSITE", "REMOTE", "HYBRID");
    private static final int MAX_LIST_ITEMS = 30;
    private static final int MAX_ITEM_LENGTH = 50;

    private final UserRepository userRepository;

    public Student getStudent(String email){
        return findStudent(email);
    }

    public Student updateStudent(String email, UpdateStudentProfileRequest req) {
        Student student = findStudent(email);

        if (req.name() != null) student.setName(req.name().trim());
        if (req.address() != null) student.setAddress(req.address().trim());
        if (req.faculty() != null) student.setFaculty(req.faculty().trim());
        if (req.department() != null) student.setDepartment(req.department().trim());
        if (req.degree() != null) student.setDegree(req.degree().trim());
        if (req.batch() != null) student.setBatch(req.batch().trim());

        if (req.skills() != null) student.setSkills(cleanList(req.skills(), "skills"));
        if (req.preferredCategories() != null) {
            student.setPreferredCategories(cleanList(req.preferredCategories(), "preferredCategories"));
        }
        if (req.workMode() != null) student.setWorkMode(cleanWorkModes(req.workMode()));

        if (req.expectedSalary() != null) student.setExpectedSalary(req.expectedSalary());
        if (req.optedOutOfMatching() != null) student.setOptedOutOfMatching(req.optedOutOfMatching());

        return userRepository.save(student);
    }

    public JobGiver getJobGiver(String email) {
        return findJobGiver(email);
    }

    public JobGiver updateJobGiver(String email, UpdateJobGiverProfileRequest req) {
        JobGiver giver = findJobGiver(email);

        if (req.name() != null) giver.setName(req.name().trim());
        if (req.address() != null) giver.setAddress(req.address().trim());
        if (req.organization() != null) giver.setOrganization(req.organization().trim());
        if (req.position() != null) giver.setPosition(req.position().trim());
        if (req.contactNumber() != null) {
            String number = req.contactNumber().trim();
            giver.setContactNumber(number.isEmpty() ? null : number); 
        }

        return userRepository.save(giver);
    }

    private Student findStudent(String email){
        User user = findUser(email);
        if (user instanceof Student student){
            return student;
        }
        throw new ApiException(HttpStatus.FORBIDDEN,"This account is not a student");
    }

    private JobGiver findJobGiver(String email) {
        User user = findUser(email);
        if (user instanceof JobGiver giver) {
            return giver;
        }
        throw new ApiException(HttpStatus.FORBIDDEN, "This account is not a job giver");
    }
    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private List<String> cleanList(List<String> input, String field) {
        Map<String, String> unique = new LinkedHashMap<>();
        for (String item : input) {
            if (item == null || item.isBlank()) continue;
            String trimmed = item.trim();
            if (trimmed.length() > MAX_ITEM_LENGTH) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "Each item in " + field + " must be " + MAX_ITEM_LENGTH + " characters or less");
            }
            unique.putIfAbsent(trimmed.toLowerCase(), trimmed);
        }
        if (unique.size() > MAX_LIST_ITEMS) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "You can add at most " + MAX_LIST_ITEMS + " items to " + field);
        }
        return new ArrayList<>(unique.values());
    }

    private List<String> cleanWorkModes(List<String> input) {
        List<String> result = new ArrayList<>();
        for (String item : input) {
            if (item == null || item.isBlank()) continue;
            String mode = item.trim().toUpperCase();
            if (!WORK_MODES.contains(mode)) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                        "workMode values must be ONSITE, REMOTE or HYBRID");
            }
            if (!result.contains(mode)) result.add(mode);
        }
        return result;
    }
}

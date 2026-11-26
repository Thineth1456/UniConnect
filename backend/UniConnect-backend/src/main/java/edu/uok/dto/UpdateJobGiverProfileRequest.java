package edu.uok.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Every field is optional. A field that is not sent (null) is left unchanged.
 * Email and role cannot be changed here.
 */
public record UpdateJobGiverProfileRequest(
        @Size(min = 2, max = 100) String name,
        @Size(max = 255) String address,
        @Size(max = 150) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String organization,
        @Size(max = 100) @Pattern(regexp = ".*\\S.*", message = "must not be blank") String position,
        @Size(max = 20) @Pattern(regexp = "^[0-9+\\- ]*$", message = "can only contain digits, +, - and spaces") String contactNumber) {
}

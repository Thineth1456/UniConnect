package edu.uok.dto;

import edu.uok.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JobGiverSignUpRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8,max = 64) String password,
    String address,
    @NotNull Role role,
    @NotBlank String organization,
    @NotBlank String position,
    String contactNumber){

}

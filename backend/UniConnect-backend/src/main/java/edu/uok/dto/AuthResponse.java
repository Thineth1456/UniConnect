package edu.uok.dto;

import edu.uok.model.User;

public record AuthResponse(String token, String message, User user) {
}

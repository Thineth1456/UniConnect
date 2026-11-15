package edu.uok.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Document(collection = "users")
public abstract class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    @JsonIgnore // never send the password hash to the frontend
    private String passwordHash;

    private Role role;

    private AccountStatus status;

    private boolean verified;

    private String address;

    private Instant createdAt = Instant.now();
}

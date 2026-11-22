package edu.uok.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
public class JobGiver extends User{
    private String organization;
    private String contactNumber;
    private String position;
    private int monthlyReferralCount = 0;
}
